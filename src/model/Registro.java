package model;

import java.io.Serializable;
import java.util.Objects;

public abstract class Registro implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNovo() {
        return id <= 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        Registro outro = (Registro) obj;
        return id > 0 && id == outro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
}
