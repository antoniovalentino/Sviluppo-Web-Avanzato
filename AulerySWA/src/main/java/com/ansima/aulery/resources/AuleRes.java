package com.ansima.aulery.resources;

import com.ansima.aulery.model.Attrezzature;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ansima.aulery.exceptions.RESTWebApplicationException;
import com.ansima.aulery.security.Logged;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

@Path("aule")
public class AuleRes {
    
    Class c = Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/aulery?noAccessToProcedureBodies=true&serverTimezone=Europe/Rome", "root", "webpass");
    
    public AuleRes() throws SQLException, ClassNotFoundException {
    }
    
    // COSTRUTTORE UTILIZZATO PER CREARE LA MAPPA DELL'OGGETTO AULA
    static Map<String, Object>createAula(ResultSet rs) {
        try {
            Map<String, Object> aula = new LinkedHashMap<>();
            aula.put("id", rs.getInt("id"));
            aula.put("nome", rs.getString("nome"));
            aula.put("note", rs.getString("note"));
            aula.put("edificio", rs.getString("edificio"));
            aula.put("piano", rs.getString("piano"));
            aula.put("zona", rs.getString("zona"));
            aula.put("capienza", rs.getString("capienza"));
            aula.put("preseElettriche", rs.getString("preseElettriche"));
            aula.put("preseDiRete", rs.getString("preseDiRete"));
            aula.put("attrezzature", rs.getString("attrezzature"));
            aula.put("areaId", rs.getString("areaId"));
            return aula;
        } catch (SQLException e) {
            throw new RESTWebApplicationException(e);
        }
    }

    //INFO AULA
    @GET
    @Path("{aulaID: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAula(@PathParam("aulaID") int aulaID) {
        try (PreparedStatement sAula = con.prepareStatement("SELECT * FROM aula WHERE id = ?")) {
            sAula.setInt(1, aulaID);
            try (ResultSet rs = sAula.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> aula = createAula(rs);
                    return Response.ok(aula).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            } catch (SQLException e) {
                throw new RESTWebApplicationException(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AuleRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    //ATTREZZATURE AULA
    @GET
    @Path("{aulaID: [0-9]+}/attrezzature")
    @Produces(MediaType.APPLICATION_JSON)
    public Response attrezzatureAula(@PathParam("aulaID") int aulaID) {
        try (PreparedStatement sAula = con.prepareStatement("SELECT attrezzature FROM aula WHERE id = ?")) {
            sAula.setInt(1, aulaID);
            try (ResultSet rs = sAula.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> attrezzature = new LinkedHashMap<>();
                    attrezzature.put("attrezzature", rs.getString("attrezzature"));
                    return Response.ok(attrezzature).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            } catch (SQLException e) {
                throw new RESTWebApplicationException(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AuleRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    //CREA AULA
    @POST
    @Logged
    @Consumes("application/json")
    @Path("crea")
    public Response creaAula(@Context UriInfo uriinfo,
            Map<String, Object> aula,
            @Context SecurityContext securityContext) {
        String query = "INSERT INTO aula (nome, note, edificio, piano, zona, capienza, preseElettriche, preseDiRete,attrezzature,areaId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, aula.get("nome").toString());
            stmt.setString(2, aula.get("note").toString());
            stmt.setString(3, aula.get("edificio").toString());
            stmt.setInt(4, Integer.parseInt(aula.get("piano").toString()));
            stmt.setString(5, aula.get("zona").toString());
            stmt.setInt(6, Integer.parseInt(aula.get("capienza").toString()));
            stmt.setInt(7, Integer.parseInt(aula.get("preseElettriche").toString()));
            stmt.setInt(8, Integer.parseInt(aula.get("preseDiRete").toString()));
                
            List<String> attrezzature = (List<String>) aula.get("attrezzature");
            Set<Attrezzature> attrezzatureSet = new HashSet<>();
            for (String attrezzatura : attrezzature) {
                attrezzatureSet.add(Attrezzature.valueOf(attrezzatura));
            }
            String attrezzatureString = "";
            Iterator<Attrezzature> iterator = attrezzatureSet.iterator();
            while (iterator.hasNext()) {
                Attrezzature attrezzatura = iterator.next();
                attrezzatureString += attrezzatura.toString();
                if (iterator.hasNext()) {
                    attrezzatureString += ",";
                }
            }
                
            stmt.setString(9, attrezzatureString);
            stmt.setInt(10, Integer.parseInt(aula.get("areaID").toString()));

            if (stmt.executeUpdate() == 1) {                  
                return Response.status(Response.Status.CREATED).entity("Aula creata con successo.").build();
            }
        } catch (SQLException ex) {
            throw new RESTWebApplicationException(ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }    
    
    //Assegna gruppo ad un aula
    @PUT
    @Logged
    @Consumes("application/json")
    @Path("{aulaID}/assegnaArea")
    public Response assegnaArea(@Context UriInfo uriinfo,
            Map<String, Object> area,
            @PathParam("aulaID") int aulaID,
            @Context SecurityContext securityContext) {
        
        String query = "UPDATE aula SET areaId = ? WHERE ID = ? ";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(area.get("areaId").toString()));
            stmt.setInt(2, aulaID);
            if (stmt.executeUpdate() == 1) {                  
                return Response.ok().build();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AuleRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    //Esporta aule in csv
    @GET
    @Logged
    @Path("esportazione")
    public Response esportaAuleCSV(@Context UriInfo uriinfo,
            @Context SecurityContext securityContext) {
        
        try (PreparedStatement sAule = con.prepareStatement("SELECT * FROM aula")) {
            List<Map<String, Object>> listaAule = new ArrayList<>();
            try (ResultSet rs = sAule.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> aula = createAula(rs);
                    listaAule.add(aula);
                }    
            } catch (SQLException e) {
                throw new RESTWebApplicationException(e);
            }
        
        // Creazione del file CSV
        File csvFile = File.createTempFile("aula", ".csv");

        try (FileWriter fileWriter = new FileWriter(csvFile);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Nome", "Note", "Edificio","Piano","Zona","Capienza","PreseElettriche","PreseDiRete","Attrezzature","AreaId"))) {

            for (Map<String, Object> aula : listaAule) {
                csvPrinter.printRecord(
                        aula.get("nome"), 
                        aula.get("note"), 
                        aula.get("edificio"), 
                        aula.get("piano"), 
                        aula.get("zona"), 
                        aula.get("capienza"), 
                        aula.get("preseElettriche"), 
                        aula.get("preseDiRete"), 
                        aula.get("attrezzature"),
                        aula.get("areaId")
                    );
            }

            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace(); // o gestisci l'eccezione in un altro modo
        }

        // Restituzione del file CSV come risposta
        return Response.ok(csvFile, MediaType.APPLICATION_OCTET_STREAM)
                       .header("Content-Disposition", "attachment; filename=\"aula.csv\"")
                       .build();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(AuleRes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    //Importa aule in csv
    @POST
    @Logged
    @Consumes("text/csv")
    @Path("importazione")
    public Response importaAuleCSV(@Context UriInfo uriinfo,
            InputStream csvFile,
            @Context SecurityContext securityContext) throws FileNotFoundException {
              
            try (Reader reader = new InputStreamReader(csvFile);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
                for (CSVRecord csvRecord : csvParser) {
                    String query = "INSERT INTO aula (nome, note, edificio, piano, zona, capienza, preseElettriche, preseDiRete,attrezzature,areaId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, csvRecord.get("Nome"));
                        stmt.setString(2, csvRecord.get("Note"));
                        
                        stmt.setString(3, csvRecord.get("Edificio"));
                        
                        stmt.setInt(4, Integer.parseInt(csvRecord.get("Piano")));
                        
                        stmt.setString(5, csvRecord.get("Zona"));
                        
                        stmt.setInt(6, Integer.parseInt(csvRecord.get("Capienza")));
                        stmt.setInt(7, Integer.parseInt(csvRecord.get("PreseElettriche")));
                        stmt.setInt(8, Integer.parseInt(csvRecord.get("PreseDiRete")));
                
                        String attrezzature = csvRecord.get("Attrezzature");
                        attrezzature = attrezzature.replace("[", "");
                        attrezzature = attrezzature.replace("]", "");
                        attrezzature = attrezzature.replace(" ", "");
                        stmt.setString(9, attrezzature);
                        stmt.setInt(10, Integer.parseInt(csvRecord.get("AreaId")));

                        stmt.executeUpdate();                 
                            
                    } catch (SQLException ex) {
                        throw new RESTWebApplicationException(ex);
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
 
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMe(
            @Context UriInfo uriinfo, String payload) {
        return Response.created(
                uriinfo.getAbsolutePathBuilder()
                        .path(this.getClass(), "getAula").build(1000))
                .build();
    }

}
