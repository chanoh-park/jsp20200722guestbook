package com.guest.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.guest.model.Message;
import com.quest.service.WriteMessageService;

/**
 * Servlet implementation class WriteServlet
 */
@WebServlet("/write")
public class WriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WriteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Message message = new Message();
		
		String name = request.getParameter("name");
		String pw = request.getParameter("password");
		String body = request.getParameter("message");
		
		if (name == null || pw == null || body == null || name.isEmpty() || pw.isEmpty() || body.isEmpty()) {
			session.setAttribute("info", "이름, 암호, 메시지를 꼭 입력하세요");
		} else {
		
			message.setGuestName(name);
			message.setPassword(pw);
			message.setMessage(body);
			
			WriteMessageService service = WriteMessageService.getInstance();
			boolean success = service.write(message);
			
			if (success) {
				session.setAttribute("info", "메시지가 등록되었습니다.");
			} else {
				session.setAttribute("info", "메시지가 등록에 실패하였습니다.");
			}
			
		}
		
		response.sendRedirect("main");
		
	}

}
