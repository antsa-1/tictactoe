package com.tauhka.tictactoe.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;

//Checks if given UUID has a match in database. Returns userName mapped to token if any.
//Lot's of different combinations for UUID tokens -> should not be easy to guess nor enumerate.
// Code does not contain blocking conditions for enumerating UUIDs endlessly, in real app. /prod.env blocking should be implemented in code or with WAF.
@Stateless(name = "UserEJBA")
public class UserEJBA {
	private static final Logger LOGGER = Logger.getLogger(UserEJBA.class.getName());
	@Resource(name = "jdbc/MariaDb")
	private DataSource portalDatasource;
	private static final String WEBSOCKET_AUTHENTICATION_QUEURY = "SELECT  a.Player_id, b.UserName,b.id FROM  active_logins a,  users b WHERE a.Login_id =? AND a.Player_id= b.id";

	public String verifyWebsocketToken(String activeLoginToken) {
		LOGGER.info("UserEJBA verifyWebsocketToken" + activeLoginToken);
		if (activeLoginToken == null) {
			return null;
		}
		PreparedStatement stmt = null;
		Connection con = null;
		UUID token = UUID.fromString(activeLoginToken);
		try {
			con = portalDatasource.getConnection();
			stmt = con.prepareStatement(WEBSOCKET_AUTHENTICATION_QUEURY);
			stmt.setString(1, token.toString());
			ResultSet res = stmt.executeQuery();
			if (res.next()) {
				String namee = res.getString("UserName");
				return namee;
			}
			// Note: ActiveLogin token is directly connected with error message in order
			// to prevent log injections.
			throw new IllegalArgumentException("UserEJBA verifyWebsocketToken: no result for:" + activeLoginToken);

		} catch (SQLException e) {
			LOGGER.severe("UserEJBA verifyWebsocketToken sqle" + e.getMessage() + e.getErrorCode());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, " UserEJBA verifyWebsocketToken finally crashed" + e);
			}
		}
		throw new RuntimeException("UserEJBA websocket runtimeException:" + token);
	}
}
