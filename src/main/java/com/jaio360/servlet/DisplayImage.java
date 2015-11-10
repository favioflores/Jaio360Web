package com.jaio360.servlet;

import com.jaio360.dao.UsuarioDAO;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Utilitarios;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.HibernateException;

public class DisplayImage extends HttpServlet implements Serializable{
 
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        InputStream sImage;

        try {
 
            String id = request.getParameter("idUsuario");
            
            System.err.println(id);
            
            UsuarioDAO objUsuarioDAO = new UsuarioDAO();
            
            Usuario objUsuario = objUsuarioDAO.obtenUsuario(new Long(id));

            byte[] bytearray = new byte[1048576];
            int size;
            sImage = new ByteArrayInputStream(objUsuario.getUsBlImagenEmpresa());
            response.reset();
            response.setContentType("image/png");
            
            while ((size = sImage.read(bytearray)) != -1) {
                response.getOutputStream().write(bytearray);
            }
            response.getOutputStream().close();
 
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
        }
    }
}