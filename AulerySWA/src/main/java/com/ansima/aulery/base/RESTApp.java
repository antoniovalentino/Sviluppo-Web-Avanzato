package com.ansima.aulery.base;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import com.ansima.aulery.jackson.ObjectMapperContextResolver;
import com.ansima.aulery.exceptions.AppExceptionMapper;
import com.ansima.aulery.security.CORSFilter;
import com.ansima.aulery.resources.AuleRes;
import com.ansima.aulery.security.AutenticazioneResource;
import com.ansima.aulery.security.AuthLoggedFilter;
import com.ansima.aulery.exceptions.JacksonExceptionMapper;
import com.ansima.aulery.resources.EventiRes;
import com.ansima.aulery.resources.CollezioniRes;

@ApplicationPath("rest")
public class RESTApp extends Application {

    private final Set<Class<?>> classes;

    public RESTApp() {
        HashSet<Class<?>> c = new HashSet<Class<?>>();
        //aggiungiamo tutte le *root resurces* (cioè quelle
        //con l'annotazione Path) che vogliamo pubblicare
        c.add(AuleRes.class);
        c.add(CollezioniRes.class);
        c.add(EventiRes.class);
        c.add(AutenticazioneResource.class);

        //aggiungiamo il provider Jackson per poter
        //usare i suoi servizi di serializzazione e 
        //deserializzazione JSON
        c.add(JacksonJsonProvider.class);

        //necessario se vogliamo una (de)serializzazione custom di qualche classe    
        c.add(ObjectMapperContextResolver.class);

        //esempio di autenticazione
        c.add(AuthLoggedFilter.class);
        
        //aggiungiamo il filtro che gestisce gli header CORS
        c.add(CORSFilter.class);

        //esempi di exception mapper, che mappano in Response eccezioni non già derivanti da WebApplicationException
        c.add(AppExceptionMapper.class);
        c.add(JacksonExceptionMapper.class);

        classes = Collections.unmodifiableSet(c);
    }

    //l'override di questo metodo deve restituire il set
    //di classi che Jersey utilizzerà per pubblicare il
    //servizio. Tutte le altre, anche se annotate, verranno
    //IGNORATE
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
