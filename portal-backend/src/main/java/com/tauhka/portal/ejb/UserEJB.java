package com.tauhka.portal.ejb;

import static com.tauhka.portal.util.Constants.ANONYM_LOGIN_TOKEN_START;
import static com.tauhka.portal.util.Constants.FORBIDDEN_WORD_PARTS;
import static com.tauhka.portal.util.Constants.LOG_PREFIX_PORTAL;
import static com.tauhka.portal.util.Constants.NULL;
import static com.tauhka.portal.util.Constants.PASSWORD_MAX_LENGTH;
import static com.tauhka.portal.util.Constants.PASSWORD_MIN_LENGTH;
import static com.tauhka.portal.util.Constants.USER_NAME_MAX_LENGTH;
import static com.tauhka.portal.util.Constants.USER_NAME_MIN_LENGTH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.tauhka.portal.pojos.User;
import com.tauhka.portal.web.security.PasswordHash;

import jakarta.annotation.Resource;
import jakarta.ejb.DuplicateKeyException;
import jakarta.ejb.Stateless;

//DB connection with register, login and logout functions
@Stateless(name = "UserEJB")
public class UserEJB {
	private static final Logger LOGGER = Logger.getLogger(UserEJB.class.getName());
	@Resource(name = "jdbc/MariaDb")
	private DataSource portalDatasource;
	private static final String USERNAME_QUERY = "select UserName,Status,Force_password_change,id,tult,secret from users where username=?";
	private static final String ACTIVE_LOGIN_INSERT = "insert into active_logins(Login_id,Player_id, User_name) values (?,?,?) ON DUPLICATE KEY UPDATE Login_id=?";
	private static final String REGISTER_INSERT = "insert into users(UserName,id,Email,Secret,tult) values (?,?,?,?,?)";
	private static final String REMOVE_ACTIVE_LOGIN = "delete from active_logins where Login_id= (?)";

	public User login(String name, String password) {
		LOGGER.info(LOG_PREFIX_PORTAL + "UserEJB login:");

		if (name == null) {
			throw new IllegalArgumentException("Login no name in ");
		}
		if (name.length() > USER_NAME_MAX_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
			throw new IllegalArgumentException("Login name or password too long");
		}
		PreparedStatement stmt = null;
		PreparedStatement insertStatement = null;
		Connection con = null;
		try {
			con = portalDatasource.getConnection();
			stmt = con.prepareStatement(USERNAME_QUERY);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String namee = rs.getString("UserName");
				String status = rs.getString("Status");
				String userId = rs.getString("id");
				String tult = rs.getString("tult");
				String secret = rs.getString("Secret");
				PasswordHash passwordHash = new PasswordHash();
				if (!passwordHash.verifyHash(password, tult, secret)) {
					throw new IllegalArgumentException("UserEJB password failure for:" + name);
				}
				boolean pwChange = rs.getBoolean("Force_passWord_change");
				rs.close();
				User user = new User();
				user.setNickName(namee);
				user.setStatus(status);
				user.setForcePasswordChange(pwChange);
				insertStatement = con.prepareStatement(ACTIVE_LOGIN_INSERT);
				String activeLoginId = UUID.randomUUID().toString();
				insertStatement.setString(1, activeLoginId);
				insertStatement.setString(2, userId);
				insertStatement.setString(3, namee);
				insertStatement.setString(4, activeLoginId);
				int res = insertStatement.executeUpdate();
				if (res > 0) {
					user.setActiveLoginId(activeLoginId);
					LOGGER.info(LOG_PREFIX_PORTAL + "UserEJB returning user " + user);
					return user;
				} else {
					LOGGER.severe(LOG_PREFIX_PORTAL + "UserEJB login fail part2");
				}
			}
			LOGGER.info(LOG_PREFIX_PORTAL + "UserEJB login fail part3");
			return null;

		} catch (SQLException e) {
			LOGGER.severe(LOG_PREFIX_PORTAL + "UserEJB sqle" + e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				LOGGER.severe(LOG_PREFIX_PORTAL + "UserEJB finally crashed" + e.getMessage());
			}
		}
		throw new RuntimeException("UserEJB was not able to fetch name for " + name);
	}

	public User register(String nickName, String password, String email) throws DuplicateKeyException {

		boolean isForbidden = FORBIDDEN_WORD_PARTS.stream().anyMatch(nickName::equalsIgnoreCase);
		if (isForbidden) {
			throw new IllegalArgumentException("Registration nickname is forbidden:" + nickName);
		}
		Optional<String> forbiddenWordOptional = FORBIDDEN_WORD_PARTS.stream().filter(forbidden -> nickName.contains(forbidden)).findAny();
		if (forbiddenWordOptional.isPresent()) {
			throw new IllegalArgumentException("Registration nickname contains forbidden word:" + forbiddenWordOptional.get() + " :" + nickName);
		}
		if (nickName == null || nickName.trim().length() < USER_NAME_MIN_LENGTH || nickName.trim().length() > USER_NAME_MAX_LENGTH) {
			throw new IllegalArgumentException("Registrating username does not match criteria:" + nickName);
		}
		if (password == null || password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
			throw new IllegalArgumentException("Registrating password does not match criteria");
		}
		String nickNamea = nickName.trim();
		PreparedStatement stmt = null;
		Connection con = null;
		try {
			con = portalDatasource.getConnection();
			stmt = con.prepareStatement(REGISTER_INSERT);
			stmt.setString(1, nickNamea);
			stmt.setString(2, UUID.randomUUID().toString());
			stmt.setString(3, email);
			PasswordHash p = new PasswordHash();
			p.generateHashes(password);
			p.getPasswordHash();
			stmt.setString(4, p.getPasswordHash());
			stmt.setString(5, p.getSaltAsHex());
			int res = stmt.executeUpdate();
			if (res == 1) {
				LOGGER.info(LOG_PREFIX_PORTAL + "UserEJB registered");
				User user = new User();
				user.setNickName(nickNamea);
				return user;
			}
			LOGGER.severe(LOG_PREFIX_PORTAL + "UserEJB register fail part3");
			return null;

		} catch (SQLException e) {
			LOGGER.severe(LOG_PREFIX_PORTAL + "UserEJB register sqle" + e.getMessage() + e.getErrorCode());
			if (e.getSQLState().equals("23000")) {
				throw new DuplicateKeyException();
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				LOGGER.severe(LOG_PREFIX_PORTAL + "UserEJB register finally crashed" + e.getMessage());
			}
		}
		throw new RuntimeException("UserEJB runtimeException " + nickName);
	}

	public boolean logout(String activeLoginToken) {
		LOGGER.info(LOG_PREFIX_PORTAL + "UserEJB logout:" + activeLoginToken);
		PreparedStatement stmt = null;
		Connection con = null;
		if (activeLoginToken == null || activeLoginToken.startsWith(ANONYM_LOGIN_TOKEN_START) || activeLoginToken.startsWith(NULL) || activeLoginToken.equals("")) {
			LOGGER.info(LOG_PREFIX_PORTAL + "UserEJB logout has no token or anonymous logout ");
			return false;
		}
		UUID token = UUID.fromString(activeLoginToken);
		try {
			con = portalDatasource.getConnection();
			stmt = con.prepareStatement(REMOVE_ACTIVE_LOGIN);
			stmt.setString(1, token.toString());
			int res = stmt.executeUpdate();
			if (res == 1) {
				LOGGER.info(LOG_PREFIX_PORTAL + "UserEJB deleted activeLogin");
				return true;
			}
			LOGGER.fine(LOG_PREFIX_PORTAL + "UserEJB logout fail part3:" + activeLoginToken);
			return false;

		} catch (SQLException e) {
			LOGGER.severe("UserEJB logout sqle" + e.getMessage() + e.getErrorCode());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, LOG_PREFIX_PORTAL + "UserEJB logout finally crashed" + e);
			}
		}
		throw new RuntimeException(LOG_PREFIX_PORTAL + "UserEJB logout runtimeException:" + token);
	}
}
