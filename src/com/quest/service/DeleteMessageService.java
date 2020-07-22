package com.quest.service;

import java.sql.Connection;

import com.guest.dao.MessageDao;
import com.guest.jdbc.ConnectionProvider;
import com.guest.jdbc.JdbcUtil;
import com.guest.model.Message;

public class DeleteMessageService {
	private static DeleteMessageService instance = new DeleteMessageService();
	
	public static DeleteMessageService getInstance() {
		return instance;
	}
	
	private DeleteMessageService() {
		
	}
	
	public String deleteMessage(int messageId, String password) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			MessageDao messageDao = MessageDao.getInstance();
			Message message = messageDao.select(conn, messageId);
			
			if (message == null) {
				return "메시지 없음";
			}
			
			if (!message.matchPassword(password)) {
				return "패스워드 불일치";
			}
			
			messageDao.delete(conn, messageId);
			conn.commit();
			
			return "삭제 성공";
		} catch (Exception e) {
			JdbcUtil.rollback(conn);
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn);
		}
		return "삭제 실패";
	}
}
