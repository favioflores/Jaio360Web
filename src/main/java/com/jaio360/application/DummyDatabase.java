package com.jaio360.application;

import com.jaio360.dao.ElementoDAO;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DummyDatabase extends Thread implements Serializable {

    private Log log = LogFactory.getLog(DummyDatabase.class);

    private static ElementoDAO objElementoDAO;

    public DummyDatabase() {
        objElementoDAO = new ElementoDAO();
    }

    @Override
    public void run() {

        while (true) {

            try {

                objElementoDAO.dummy();

                this.sleep(1800000); // a minute

            } catch (Exception ex) {
                log.error(ex);
                this.stop();
            }

        }

    }

}
