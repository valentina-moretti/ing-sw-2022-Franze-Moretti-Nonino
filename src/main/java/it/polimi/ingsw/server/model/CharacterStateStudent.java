package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.gamePojo.CharacterPojo;
import it.polimi.ingsw.common.gamePojo.CharacterStudentPojo;
import it.polimi.ingsw.common.gamePojo.ColourPawn;
import it.polimi.ingsw.common.gamePojo.PawnsMapPojo;

public class CharacterStateStudent extends CharacterState{
    private PawnsMap students;

    public CharacterStateStudent(int characterId, int actualCost){
        super(characterId, actualCost);
        students=new PawnsMap();
    }

    public void removeStudent (ColourPawn colour){
        students.remove(colour);
    }

    public void addStudent(ColourPawn student){
        students.add(student);
    }

    public PawnsMap getAllStudents(){
        PawnsMap tmp = new PawnsMap();
        tmp.add(students);
        return tmp;
    }

    @Override
    public CharacterStudentPojo toPojo(){
        CharacterStudentPojo characterStudentPojo = new CharacterStudentPojo();
        characterStudentPojo.setCharacterId(this.getCharacterId());
        characterStudentPojo.setActualCost(this.getCost());
        characterStudentPojo.setStudents(new PawnsMapPojo(this.students));

        return characterStudentPojo;
    }

}
