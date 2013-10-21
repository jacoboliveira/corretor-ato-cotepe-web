/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ato.cotepe.entities;

/**
 *
 * @author jacoboliveira
 */
public class TipoE14 {
    private String ccf;
    private String coo;

    public TipoE14() {
    }

    /**
     * @return the ccf
     */
    public String getCcf() {
        return ccf;
    }

    /**
     * @param ccf the ccf to set
     */
    public void setCcf(String ccf) {
        this.ccf = ccf;
    }

    /**
     * @return the coo
     */
    public String getCoo() {
        return coo;
    }

    /**
     * @param coo the coo to set
     */
    public void setCoo(String coo) {
        this.coo = coo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.ccf != null ? this.ccf.hashCode() : 0);
        hash = 67 * hash + (this.coo != null ? this.coo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoE14 other = (TipoE14) obj;
        if ((this.ccf == null) ? (other.ccf != null) : !this.ccf.equals(other.ccf)) {
            return false;
        }
        if ((this.coo == null) ? (other.coo != null) : !this.coo.equals(other.coo)) {
            return false;
        }
        return true;
    }
    
    
}
