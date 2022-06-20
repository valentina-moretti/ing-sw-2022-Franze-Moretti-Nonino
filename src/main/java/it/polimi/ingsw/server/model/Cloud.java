package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.CloudPojo;
import it.polimi.ingsw.common.gamePojo.PawnsMapPojo;

public class Cloud {

    private PawnsMap students;
    private int cloudId;

    public Cloud(int cloudId){
        this.cloudId=cloudId;
        students=new PawnsMap();
    }

    public int getCloudId() {
        return cloudId;
    }

    public PawnsMap getStudents() {
        return this.students;
    }

    /**
     * Removes all the students from the cloud
     * @return PawnsMap the students that were situated on the cloud
     */
    public PawnsMap clearCloud(){
        PawnsMap oldStudents = this.getStudents().clone();
        this.students = new PawnsMap();
        return oldStudents;
    }

    /**
     * Returns the CloudPojo representing this*/
    public CloudPojo toPojo(){
        CloudPojo pojoCloudPojo = new CloudPojo();
        pojoCloudPojo.setCloudId(this.getCloudId());
        pojoCloudPojo.setStudents(new PawnsMapPojo(this.students));
        return pojoCloudPojo;
    }
}
