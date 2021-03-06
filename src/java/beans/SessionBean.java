/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.websocket.Session;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import managers.LogManager;

/**
 *
 * @author Sapan
 */
@Entity
@Table(name = "session_table")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SessionBean.findAll", query = "SELECT s FROM SessionBean s")
    , @NamedQuery(name = "SessionBean.findById", query = "SELECT s FROM SessionBean s WHERE s.id = :id")
    , @NamedQuery(name = "SessionBean.findByFromYear", query = "SELECT s FROM SessionBean s WHERE s.fromYear = :fromYear")
    , @NamedQuery(name = "SessionBean.findByToYear", query = "SELECT s FROM SessionBean s WHERE s.toYear = :toYear")
    , @NamedQuery(name = "SessionBean.findBySemType", query = "SELECT s FROM SessionBean s WHERE s.semType = :semType")})
public class SessionBean extends Bean implements Serializable {

    private static final long serialVersionUID = 1L;
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Expose
    @Basic(optional = false)
    @Column(name = "from_year")
    private int fromYear;
    @Expose
    @Basic(optional = false)
    @Column(name = "to_year")
    private int toYear;
    @Expose
    @Basic(optional = false)
    @Column(name = "sem_type")
    private String semType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionId")
    private Collection<CsfBean> csfBeanCollection;

    public SessionBean() {
    }

    public SessionBean(Long id) {
        this.id = id;
    }

    public SessionBean(Long id, int fromYear, int toYear, String semType) {
        this.id = id;
        this.fromYear = fromYear;
        this.toYear = toYear;
        this.semType = semType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public String getSemType() {
        return semType;
    }

    public void setSemType(String semType) {
        this.semType = semType;
    }

    @XmlTransient
    public Collection<CsfBean> getCsfBeanCollection() {
        return csfBeanCollection;
    }

    public void setCsfBeanCollection(Collection<CsfBean> csfBeanCollection) {
        this.csfBeanCollection = csfBeanCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SessionBean)) {
            return false;
        }
        SessionBean other = (SessionBean) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.SessionBean[ id=" + id + " ]";
    }

    public void persist(Object object) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public SessionBean findById() {
        TypedQuery<SessionBean> query = getEntityManager().createNamedQuery("SessionBean.findById", SessionBean.class);
        query.setParameter("id", id);
        SessionBean sb = null;
        try {
            sb = query.getSingleResult();
        } catch (NoResultException e) {
            LogManager.log("No result found (SessionBean:findById()) for id " + id + " " + e);
            return null;
        } catch (NonUniqueResultException e) {
            LogManager.log("More than one result found (SessionBean:findById()) for id " + id + " " + e);
            return null;
        }
        return sb;
    }

}
