package com.rekmock;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rekmock.util.ValidationUtils.empty;

/**
 * Created by Faleh Omar on 01/21/2015.
 */
public abstract class FileSystemDataSetStore extends DataSetStore {
    public static final String DATA_FILE_NAME = "data";
    public static final String DATA_FILE_PATTERN = DATA_FILE_NAME + "_(\\d+)?\\.\\w+";
    private static final Pattern DATA_FILE_REGEX = Pattern.compile(DATA_FILE_PATTERN);

    private Map<String, CallResultMap> data;

    private String dataSetRoot;


    class CallResultMap {

        // mock name (method) -> object
        Map<String, List> data;

        Object consume(String name) {
            if (data == null)
                return null;
            final List list = data.get(name);
            if (list == null)
                return null;
            return list.isEmpty() ? null : list.remove(0);
        }

        public CallResultMap create() {
            return new CallResultMap();
        }
    }


    protected void buildDataset() {

        final String dataSetRoot = getDataSetRoot();
        if (empty(dataSetRoot)) {
            throw new RekMokException("dataSetRoot is required");
        }
        // data set root has to be an existing directory
        File rootDir = getDataSetRootAsFile();
        if (!rootDir.exists()) {
            throw new RekMokException(dataSetRoot + " rootDir does not exist");
        }

        this.data = new HashMap<String, CallResultMap>();
        for (File testCaseDir : getDirs(rootDir)) {

            CallResultMap callResultMap = new CallResultMap();
            this.data.put(testCaseDir.getName(), callResultMap);

            populate(callResultMap, new File(rootDir, testCaseDir.getName()));
        }

    }

    private File getDataSetRootAsFile() {
        return new File(this.dataSetRoot);
    }

    public void setDataSetRoot(String dataSetRoot) {
        this.dataSetRoot = dataSetRoot;
    }

    public String getDataSetRoot() {
        return this.dataSetRoot;
    }

    /**
     * <pre>
     *  testCaseName ->
     *                 |
     *                 . -> call name ->
     *                                  . -> data_@{order}
     * </pre>
     *
     * @param data
     * @param rootDir
     */
    private void populate(CallResultMap data, File rootDir) {

        final File[] subDirs = getDirs(rootDir);

        if (subDirs != null)
            populateCallResultMap(subDirs, data);

    }

    private List<CallResultMap> getCallResults(File[] files) {
        List callResults = new ArrayList();
        for (File file : files) {

            if (file.getName().matches(DATA_FILE_PATTERN)) {
                callResults.add(readFile(file));
            }
        }
        return callResults;
    }

    private File[] getFiles(File dir) {
        return dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {

                return new File(dir, name).isFile();
            }
        });
    }

    private File[] getDirs(File dir) {
        return dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });
    }

    private void populateCallResultMap(File[] subDirs, CallResultMap callResultMap) {

        for (File dir : subDirs) {

            final File[] files = getFiles(dir);

            if (files.length == 0) {
                continue;
            }

            if (callResultMap.data == null)
                callResultMap.data = new HashMap<String, List>();

            callResultMap.data.put(dir.getName(), getCallResults(files));
        }
    }

    @Override
    synchronized public void save(String testCaseName, Class type, String methodName, InputStream inputStream, String fileType) {
        File testCaseDir = getTestCaseDir(testCaseName);
        File nameFile = new File(testCaseDir, methodName);

        int index = -1;
        if (!nameFile.exists()) {
            nameFile.mkdirs();
        } else {

            File[] files = getFiles(nameFile);
            if (files == null)
                index = 0;
            else
                for (File file : files) {
                    String fileName = file.getName();
                    Matcher matcher = DATA_FILE_REGEX.matcher(fileName);
                    if (matcher.find()) {
                        String si = matcher.group(1);
                        int i = Integer.parseInt(si);
                        index = Math.max(i, index);
                    }
                }
        }

        String dataFileName = DATA_FILE_NAME + "_" + (index + 1)+"."+fileType;

        writeToFile(inputStream, nameFile, dataFileName);

    }

    protected File getTestCaseDir(String testCaseName) {
        return new File(getDataSetRootAsFile(), testCaseName);
    }

    protected void writeToFile(InputStream content, File dir, String fileNme) {
        BufferedOutputStream outputStream = null;
        try {

            outputStream = new BufferedOutputStream(new FileOutputStream(new File(dir, fileNme)));
            IOUtils.copy(content, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Override
    synchronized public InputStream load(String testCaseName, Class type, String methodName) {
        CallResultMap callResultMap = this.data.get(testCaseName);
        if (callResultMap == null)
            return null;
        return (InputStream) callResultMap.consume(methodName);
    }


    private InputStream readFile(File file) {
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RekMokException("file not found", e);
        }
        return inputStream;
    }
}
