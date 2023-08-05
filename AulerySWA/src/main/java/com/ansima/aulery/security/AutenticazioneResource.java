package com.ansima.aulery.security;

import java.sql.*;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("auth")
public class AutenticazioneResource {

    Class driver = Class.forName("com.mysql.jdbc.Driver");
    Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/aulery?noAccessToProcedureBodies=true&serverTimezone=Europe/Rome", "root", "webpass");

    public AutenticazioneResource() throws ClassNotFoundException, SQLException {
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response Login(@Context UriInfo uriinfo,
                            @FormParam("username") String username,
                            @FormParam("password") String password) {
        try {
            int amministratoreID = authenticate(username, password);
            if (amministratoreID > 0) {
                String authToken = issueToken(uriinfo, username);
                try (PreparedStatement statement = conn.prepareStatement("UPDATE amministratoreRest SET token=? WHERE ID=?")) {
                    statement.setString(1, authToken);
                    statement.setInt(2, amministratoreID);
                    statement.executeUpdate();
                }
                return Response.ok(authToken)
                        .cookie(new NewCookie("token", authToken))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Logged
    @DELETE
    @Path("/logout")
    public Response Logout(@Context HttpServletRequest request) {
        try {
            //estraiamo i dati inseriti dal nostro LoggedFilter...
            String token = (String) request.getAttribute("token");
            if (token != null) {
                revokeToken(token);
            }
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    private int authenticate(String username, String password) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT ID FROM amministratoreRest WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, Helper.encryptPassword(password));
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            return 0;
        }
    }

    private String issueToken(UriInfo context, String username) {
        return UUID.randomUUID().toString();
    }

    private void revokeToken(String token) {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE amministratoreRest SET token=NULL WHERE token=?")) {
            stmt.setString(1, token);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
