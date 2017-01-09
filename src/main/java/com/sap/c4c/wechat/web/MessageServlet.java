package com.sap.c4c.wechat.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.sap.c4c.wechat.model.Customer;
import com.sap.security.core.server.csi.IXSSEncoder;
import com.sap.security.core.server.csi.XSSEncoder;

/**
 * Servlet implementing simplest possible hello world application for SAP HANA
 * Cloud Platform.
 */
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DataSource ds;
	private EntityManagerFactory emf;

	/** {@inheritDoc} */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void init() throws ServletException {
		Connection connection = null;
		try {
			InitialContext ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");

			Map properties = new HashMap();
			properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
			emf = Persistence.createEntityManagerFactory("persistence-with-jpa", properties);
		} catch (NamingException e) {
			throw new ServletException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void destroy() {
		emf.close();
	}

	/** {@inheritDoc} */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Append table that lists all persons
		EntityManager em = emf.createEntityManager();

		try {
			@SuppressWarnings("unchecked")
			List<Customer> resultList = em.createNamedQuery("AllCustomers").getResultList();
			response.getWriter().println("<p><table border=\"1\"><tr><th colspan=\"3\">"
					+ (resultList.isEmpty() ? "" : resultList.size() + " ") + "Entries in the Database</th></tr>");
			if (resultList.isEmpty()) {
				response.getWriter().println("<tr><td colspan=\"3\">Database is empty</td></tr>");
			} else {
				response.getWriter().println("<tr><th>First name</th><th>Last name</th><th>Id</th></tr>");
			}
			IXSSEncoder xssEncoder = XSSEncoder.getInstance();
			for (Customer p : resultList) {
				response.getWriter().println("<tr><td>" + xssEncoder.encodeHTML(p.firstName) + "</td><td>"
						+ xssEncoder.encodeHTML(p.lastName) + "</td><td>" + p.id + "</td></tr>");
			}
			response.getWriter().println("</table></p>");
		} finally {
			em.close();
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Extract name of person to be added from request
			String firstName = request.getParameter("FirstName");
			String lastName = request.getParameter("LastName");

			// ADD PERSON IF NAME IS NOT NULL/EMPTY
			EntityManager em = emf.createEntityManager();
			try {
				if (firstName != null && lastName != null && !firstName.trim().isEmpty()
						&& !lastName.trim().isEmpty()) {
					Customer customer = new Customer(firstName, lastName);
					em.getTransaction().begin();
					em.persist(customer);
					em.getTransaction().commit();
				}
			} finally {
				em.close();
			}
		} catch (Exception e) {
			response.getWriter().println("Persistence operation failed with reason: " + e.getMessage());
		}
	}
}
