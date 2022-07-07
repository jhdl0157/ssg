import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@WebServlet(name = "Hello", value = "/Hello")
public class Hello extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        LocalDateTime today =LocalDateTime.now();
        out.println("<html>" +
                "<body>" +
                "<h1 align=center>안녕하세요! </h1><br>" +
                "<br>"+"현재시간은 :" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(today)+"입니다!!! " + "<br>"+"HelloWorld"+
                "</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
