package com.ansima.aulery.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ansima.aulery.exceptions.RESTWebApplicationException;
import com.ansima.aulery.security.Logged;
import jakarta.ws.rs.PUT;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

@Path("eventi")
public class EventiRes {
    
    Class c = Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/aulery?noAccessToProcedureBodies=true&serverTimezone=Europe/Rome", "root", "webpass");
    
    public EventiRes() throws SQLException, ClassNotFoundException {
    }
    
    // COSTRUTTORE UTILIZZATO PER CREARE LA MAPPA DELL'OGGETTO EVENTO
    static Map<String, Object>createEvento(ResultSet rs) {
        try {
            Map<String, Object> evento = new LinkedHashMap<>();
            evento.put("id", rs.getInt("id"));
            evento.put("nome", rs.getString("nome"));
            evento.put("descrizione", rs.getString("descrizione"));
            evento.put("data", rs.getString("data"));
            evento.put("oraInizio", rs.getString("oraInizio"));
            evento.put("oraFine", rs.getString("oraFine"));
            evento.put("tipologiaEvento", rs.getString("tipologiaEvento"));
            evento.put("personaId", rs.getString("personaId"));
            evento.put("aulaId", rs.getString("aulaId"));
            evento.put("corsoId", rs.getString("corsoId"));
            return evento;
        } catch (SQLException e) {
            throw new RESTWebApplicationException(e);
        }
    }

    //REST INFO EVENTO
    @GET
    @Path("{eventoID: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvento(@PathParam("eventoID") int eventoID) {
        try (PreparedStatement sEvento = con.prepareStatement("SELECT * FROM evento WHERE id = ?")) {
            sEvento.setInt(1, eventoID);
            try (ResultSet rs = sEvento.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> evento = createEvento(rs);
                    return Response.ok(evento).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            } catch (SQLException e) {
                throw new RESTWebApplicationException(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventiRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    //CREA EVENTO
    @POST
    @Logged
    @Consumes("application/json")
    @Path("crea")
    public Response creaEvento(@Context UriInfo uriinfo,
            Map<String, Object> evento,
            @Context SecurityContext securityContext) {
        String query = "INSERT INTO evento (nome, descrizione, data, oraInizio, oraFine, tipologiaEvento, personaID, aulaID, corsoID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, evento.get("nome").toString());
            stmt.setString(2, evento.get("descrizione").toString());
            stmt.setDate(3, java.sql.Date.valueOf(evento.get("data").toString()));
            String oraInizio = evento.get("oraInizio").toString() + ":00";
            stmt.setTime(4, java.sql.Time.valueOf(oraInizio));
            String oraFine = evento.get("oraFine").toString() + ":00";
            stmt.setTime(5, java.sql.Time.valueOf(oraFine));
            stmt.setString(6, evento.get("tipologiaEvento").toString());
            stmt.setInt(7, Integer.parseInt(evento.get("personaID").toString()));
            stmt.setInt(8, Integer.parseInt( evento.get("aulaID").toString()));
            stmt.setInt(9, Integer.parseInt( evento.get("corsoID").toString()));
            if (stmt.executeUpdate() == 1) {                  
                return Response.status(Response.Status.CREATED).entity("Evento creato con successo.").build();
            }
        } catch (SQLException ex) {
            throw new RESTWebApplicationException(ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    //MODIFICA EVENTO
    @PUT
    @Logged
    @Path("{eventoID: [0-9]+}/modificaEvento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEvento(@Context UriInfo uriinfo,
            @PathParam("eventoID") int eventoID,
            Map<String, Object> evento,
            @Context SecurityContext securityContext
            ) {
        
        Map<String, Object> oldEvento = new HashMap<>();
        try (PreparedStatement sEvento = con.prepareStatement("SELECT * FROM evento WHERE id = ?")) {
            sEvento.setInt(1, eventoID);
            try (ResultSet rs = sEvento.executeQuery()) {
                if (rs.next()) {
                    oldEvento = createEvento(rs);
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            } catch (SQLException e) {
                throw new RESTWebApplicationException(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventiRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String query = "UPDATE evento SET nome = ?, descrizione = ?, data = ?, oraInizio = ?, oraFine = ?, tipologiaEvento = ?, personaId = ?, aulaId = ?, corsoId = ? WHERE ID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            if(!"".equals(evento.get("nome").toString())){
                stmt.setString(1, evento.get("nome").toString());
            } else {
                stmt.setString(1, oldEvento.get("nome").toString());
            }
            
            if(!"".equals(evento.get("descrizione").toString())){
                stmt.setString(2, evento.get("descrizione").toString());
            } else {
                stmt.setString(2, oldEvento.get("descrizione").toString());
            }
            
            if(!"".equals(evento.get("data").toString())){
                stmt.setDate(3, java.sql.Date.valueOf(evento.get("data").toString()));
            } else {
                stmt.setDate(3, java.sql.Date.valueOf(oldEvento.get("data").toString()));
            }
            
            if(!"".equals(evento.get("oraInizio").toString())){
                String oraInizio = evento.get("oraInizio").toString() + ":00";
                stmt.setTime(4, java.sql.Time.valueOf(oraInizio));            
            } else {
                String oraInizio = oldEvento.get("oraInizio").toString() + ":00";
                stmt.setTime(4, java.sql.Time.valueOf(oraInizio));
            }
            
            if(!"".equals(evento.get("oraFine").toString())){
                String oraFine = evento.get("oraFine").toString() + ":00";
                stmt.setTime(5, java.sql.Time.valueOf(oraFine));            
            } else {
                String oraFine = oldEvento.get("oraFine").toString() + ":00";
                stmt.setTime(5, java.sql.Time.valueOf(oraFine)); 
            }
            
            if(!"".equals(evento.get("tipologiaEvento").toString())){
                stmt.setString(6, evento.get("tipologiaEvento").toString());
            } else {
                stmt.setString(6, oldEvento.get("tipologiaEvento").toString());
            }
            
            if(!"".equals(evento.get("personaID").toString())){
                stmt.setInt(7, Integer.parseInt(evento.get("personaID").toString()));
            } else {
                stmt.setInt(7, Integer.parseInt(evento.get("personaId").toString()));
            }
            
            if(!"".equals(evento.get("aulaID").toString())){
                stmt.setInt(8, Integer.parseInt( evento.get("aulaID").toString()));
            } else {
                stmt.setInt(8, Integer.parseInt( oldEvento.get("aulaId").toString()));
            }
            
            if(!"".equals(evento.get("corsoID").toString())){
                stmt.setInt(9, Integer.parseInt( evento.get("corsoID").toString()));
            } else {
                stmt.setInt(9, Integer.parseInt( oldEvento.get("corsoID").toString()));
            }
            
            stmt.setInt(10, eventoID);

            if (stmt.executeUpdate() == 1) {                  
                return Response.ok().build();
            }
        } catch (SQLException ex) {
            throw new RESTWebApplicationException(ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    //Esporta eventi in csv
    @GET
    @Logged
    @Path("esportazione/{dataI}/{dataF}")
    public Response esprtaEventiCSV(@Context UriInfo uriinfo,
            @PathParam("dataI") String dataI,
            @PathParam("dataF") String dataF) throws IOException {
        LocalDate dataInizio = LocalDate.parse(dataI);
        LocalDate dataFine = LocalDate.parse(dataF);
        try (PreparedStatement sEventiByTwoDates = con.prepareStatement("SELECT * FROM evento WHERE data BETWEEN ? AND ?")) {
            sEventiByTwoDates.setDate(1, java.sql.Date.valueOf(dataInizio));
            sEventiByTwoDates.setDate(2, java.sql.Date.valueOf(dataFine));
            List<Map<String, Object>> listaEventi = new ArrayList<>();
            try (ResultSet rs = sEventiByTwoDates.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> evento = createEvento(rs);
                    listaEventi.add(evento);
                }    
            } catch (SQLException e) {
                throw new RESTWebApplicationException(e);
            }
        
        // Creazione del file CSV
        File csvFile = File.createTempFile("eventi", ".csv");

        try (FileWriter fileWriter = new FileWriter(csvFile);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Nome", "Descrizione", "Data","OraInizio","OraFine","TipologiaEvento","PersonaId","AulaId","CorsoId"))) {

            for (Map<String, Object> evento : listaEventi) {
                csvPrinter.printRecord(
                        evento.get("nome"), 
                        evento.get("descrizione"), 
                        evento.get("data"), 
                        evento.get("oraInizio"), 
                        evento.get("oraFine"), 
                        evento.get("tipologiaEvento"), 
                        evento.get("personaId"), 
                        evento.get("aulaId"), 
                        evento.get("corsoId")
                    );
            }

            csvPrinter.flush();
        // Restituzione del file CSV come risposta
        return Response.ok(csvFile, MediaType.APPLICATION_OCTET_STREAM)
                       .header("Content-Disposition", "attachment; filename=\"eventi.csv\"")
                       .build();

        } catch (IOException ex) {
            Logger.getLogger(EventiRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        }catch (SQLException ex) {
            Logger.getLogger(EventiRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    private Response eventiUriList(@Context UriInfo uriInfo, ResultSet rs) throws SQLException {
        Set<String> eventi = new HashSet<>();
        while (rs.next()) {
            eventi.add(uriInfo.getBaseUriBuilder().path(EventiRes.class).path(Integer.toString(rs.getInt("eventoID"))).build().toString());
        }
        if (eventi.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(eventi).build();
        }
    }
    
    //REST LISTA EVENTI PROSSIME 3 ORE
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("aree/{areaID: [0-9]+}/prossime3ore")
    public Response getEventiTreOre(
            @Context UriInfo uriInfo,
            @PathParam("areaID") int areaID) {
        try (PreparedStatement sEventibyNextThreeHours = con.prepareStatement("SELECT ID AS eventoID FROM evento WHERE aulaID IN (SELECT ID FROM aula WHERE areaID = ?) AND oraInizio >= ? AND oraInizio <= ? OR oraFine >= ? AND oraFine <= ?  AND data BETWEEN ? AND ?")) {
            LocalTime currentTime = LocalTime.now();
            LocalTime threeHoursLater = currentTime.plusHours(3);
            LocalDate giorno = LocalDate.now();
            sEventibyNextThreeHours.setInt(1, areaID);
            sEventibyNextThreeHours.setTime(2, java.sql.Time.valueOf(currentTime));
            sEventibyNextThreeHours.setTime(3, java.sql.Time.valueOf(threeHoursLater));
            sEventibyNextThreeHours.setTime(4, java.sql.Time.valueOf(currentTime));
            sEventibyNextThreeHours.setTime(5, java.sql.Time.valueOf(threeHoursLater));
            sEventibyNextThreeHours.setDate(6, java.sql.Date.valueOf(giorno));
            sEventibyNextThreeHours.setDate(7, java.sql.Date.valueOf(giorno.plusDays(1)));
            try (ResultSet rs = sEventibyNextThreeHours.executeQuery()) {
                return eventiUriList(uriInfo, rs);
            } catch (SQLException e) {
                throw new RESTWebApplicationException(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CollezioniRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    //REST LISTA EVENTI CON AULA E SETTIMANA
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("aule/{aulaID: [0-9]+}/settimanale/{data}")
    public Response getEventoByAulaAndSettimana(
            @Context UriInfo uriInfo,
            @PathParam("aulaID") int aulaID,
            @PathParam("data") String data ) {
        
        try {
        // Converte la stringa 'giornoString' in un oggetto LocalDate
            LocalDate giorno = LocalDate.parse(data);
            try (PreparedStatement sEventiByAulaAndSettimana = con.prepareStatement("SELECT ID AS eventoID FROM evento WHERE aulaID = ? AND data BETWEEN ? AND ?")) {
                sEventiByAulaAndSettimana.setInt(1, aulaID);
                LocalDate firstDayOfWeek = giorno.with(DayOfWeek.MONDAY);
                LocalDate lastDayOfWeek = giorno.with(DayOfWeek.SUNDAY);
                sEventiByAulaAndSettimana.setDate(2, java.sql.Date.valueOf(firstDayOfWeek));
                sEventiByAulaAndSettimana.setDate(3, java.sql.Date.valueOf(lastDayOfWeek));
                try (ResultSet rs = sEventiByAulaAndSettimana.executeQuery()) {
                    return eventiUriList(uriInfo, rs);
                } catch (SQLException e) {
                    throw new RESTWebApplicationException(e);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CollezioniRes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (DateTimeParseException e) {
            // Gestisci l'eccezione per la conversione della data
            return Response.status(Response.Status.BAD_REQUEST).entity("Data non valida. Usa il formato 'yyyy-MM-dd'.").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMe(
            @Context UriInfo uriinfo, String payload) {
        return Response.created(
                uriinfo.getAbsolutePathBuilder()
                        .path(this.getClass(), "getEvento").build(1000))
                .build();
    }
}