package com.jp.boiler.base.domain.idgenerator;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class SeqGenerator implements IdentifierGenerator, Configurable {

    public static final String SEQ_GENERATOR_PARAM_KEY = "procedureParam";

    private String procedureParam;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object o) throws HibernateException {
        Connection connection = session.connection();
        String query = "SELECT FNC_SEQ_KEY(?) AS Id FROM DUAL";

        try{
            final PreparedStatement stmt = connection.prepareStatement(query);

            try{
                stmt.setString(1, procedureParam);
                final ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    log.info(procedureParam + " ID : " + rs.getString("Id"));
                    return rs.getString("Id");
                }
            }catch (SQLException throwables){
                log.error("[SEQ]", throwables);
            } finally {
                stmt.close();
            }

        } catch (SQLException e) {
            throw new HibernateException("Unable to generate Stock Code Sequence");
        }
        return null;
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        this.procedureParam = ConfigurationHelper.getString(SEQ_GENERATOR_PARAM_KEY, params);
    }
}
