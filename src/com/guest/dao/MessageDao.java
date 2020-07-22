package com.guest.dao;

import java.sql.*;
import java.util.*;

import com.guest.jdbc.JdbcUtil;
import com.guest.model.Message;

public class MessageDao {
	private static MessageDao messageDao = new MessageDao();
	public static MessageDao getInstance() {
		return messageDao;
	}
	
	private MessageDao() {
		
	}
	
	// database 테이블에 값 추가 메소드
	public int insert(Connection conn, Message message) throws SQLException {
		
		// 1. 클래스 로딩 : listener 에서 이미 로딩됨
		// 2. 연결 생성 : parameter로 받음
		// 3. statement 생성 : 메소드 내에서 생성
		// 4. 쿼리 실행 : 메소드 내에서 생성
		// 5. 결과 처리 : 호출한 곳에서 결과 처리
		// 6. 자원 종료 : 
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("INSERT INTO guestbook_message " + "(guest_name, password, message) values (?, ?, ?)");
			
			pstmt.setString(1, message.getGuestName());
			pstmt.setString(2, message.getPassword());
			pstmt.setString(3, message.getMessage());
			
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	// database 테이블에 있는 값 조회 메소드
	public Message select(Connection conn, int messageId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM guestbook_message WHERE message_id = ?");
			
			pstmt.setInt(1, messageId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return makeMessageFromResultSet(rs);
			} else {
				return null;
			}
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	// ResultSet에서 데이터를 읽어와 Message를 생성한다. select() 메소드와 selectList() 메소드에서 사용.
	private Message makeMessageFromResultSet(ResultSet rs) throws SQLException {
		Message message = new Message();
		message.setId(rs.getInt("message_id"));
		message.setGuestName(rs.getString("guest_name"));
		message.setPassword(rs.getString("Password"));
		message.setMessage(rs.getString("message"));
		
		return message;
	}
	
	// database 테이블에 있는 값들의 개수 출력 메소드
	public int selectCount(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM guestbook_message");
			rs.next();
			return rs.getInt(1);
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}
	
	// database 테이블의 데이터를 message_id 내림차순으로 정렬해서 죄회하는 메소드
	public List<Message> selectList(Connection conn, int firstRow, int endRow) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM guestbook_message " + "ORDER BY message_id DESC limit ?, ?");
			
			pstmt.setInt(1, firstRow - 1);
			pstmt.setInt(2, endRow - firstRow + 1);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				List<Message> messageList = new ArrayList<Message>();
				do {
					messageList.add(makeMessageFromResultSet(rs));
				} while (rs.next());
				return messageList;
			} else {
				return Collections.emptyList();
			}
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	// database 테이블의 값을 삭제하는 메소드
	public int delete(Connection conn, int messageId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("DELETE FROM guestbook_message WHERE message_id = ?");
			
			pstmt.setInt(1, messageId);
			
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
	
}
