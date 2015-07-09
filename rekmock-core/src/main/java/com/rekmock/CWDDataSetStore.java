package com.rekmock;

/**
 * Created by Faleh Omar on 01/21/2015.
 */
public class CWDDataSetStore extends FileSystemDataSetStore {


    public CWDDataSetStore() {
        if(System.getProperty("rekmock.datasetRoot")!= null){
            super.setDataSetRoot(System.getProperty("rekmock.datasetRoot"));
        } else {
        super.setDataSetRoot(System.getProperty("user.dir"));
        }
        buildDataset();
    }


    @Override
    public void setDataSetRoot(String dataSetRoot) {
        throw new RekMokException("cannot set dataset root on CWDDataSetStore");
    }
}
