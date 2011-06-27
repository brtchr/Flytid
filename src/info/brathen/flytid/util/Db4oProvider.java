package info.brathen.flytid.util;

import java.io.Serializable;
import java.util.List;

import android.content.Context;

import com.db4o.ObjectSet;

public class Db4oProvider <T extends Serializable> extends Db4oHelper {
    public Class<T> persistentClass;

    public Db4oProvider( Class<T> persistentClass, Context ctx ) {
        super( ctx );
        this.persistentClass = persistentClass;
    }

    public void store(List<T> list) {
        for(T t: list) {
        	db().store(t);
        }
    }

    public void delete(T obj) {
         db().delete( obj );
    }

    public List<T> findAll() {
        return db().query( persistentClass );
    }
    
    public void deleteAll() {
        ObjectSet<T> results = db().query( persistentClass );
        for (T t : results) {
        	db().delete( t );
		}
    }
}
