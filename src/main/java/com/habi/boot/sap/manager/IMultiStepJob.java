package com.habi.boot.sap.manager;

public interface  IMultiStepJob {
    public boolean runNextStep();

    String getName();

    public void cleanUp();
}
