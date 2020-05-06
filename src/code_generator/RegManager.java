package code_generator;

import java.lang.reflect.Array;
import java.util.Arrays;

public class RegManager {
    private Register registerArray[];

    public RegManager() {
        this.registerArray = new Register[10];
        for(int i=0; i<registerArray.length; i++)
            registerArray[i] = new Register();
    }

    public Register getByIndex(int index){
        if(registerArray.length <= index && 0 < index)
            return null;
        return registerArray[index];
    }

    public Register getByName(String name){
        for(int i=0; i<registerArray.length; i++){
            if(registerArray[i].getName().equals(name)) {
                return registerArray[i];
            }
        }
        return null;
    }

    public int getFreeReg(){
        for(int i=0; i<registerArray.length; i++){
            if(!registerArray[i].getInUse()) {
                registerArray[i].setInUse(true);
                return i;
            }
        }
        return -1;
    }

    public int getFreeRegWithName(String name){
        int reg;
        if((reg = getFreeReg()) < 0)
            return reg;

        registerArray[reg].setName(name);
        return reg;
    }

    public void freeReg(int index){
        getByIndex(index).setInUse(false);
        getByIndex(index).setName(null);
    }

    @Override
    public String toString() {
        return "RegManager{" +
                "registerArray=" + Arrays.toString(registerArray) +
                '}';
    }
}