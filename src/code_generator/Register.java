package code_generator;

public class Register {
    private String name;
    private Boolean inUse;

    public Register() {
        this.name = null;
        this.inUse = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    @Override
    public String toString() {
        return  "{name='" + name + '\'' +
                ", inUse=" + String.valueOf(inUse) +
                '}';
    }
}
