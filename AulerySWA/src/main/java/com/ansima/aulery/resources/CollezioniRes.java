package com.ansima.aulery.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ansima.aulery.exceptions.RESTWebApplicationException;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

@Path("collezioni")
public class CollezioniRes {
    
    Class c = Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/aulery?noAccessToProcedureBodies=true&serverTimezone=Europe/Rome", "root", "webpass");
    
    public CollezioniRes() throws SQLException, ClassNotFoundException {
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
    
}