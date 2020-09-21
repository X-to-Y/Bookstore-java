package cn.xh.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.javafx.collections.MappingChange.Map;

import cn.xh.domain.Administrator;
import cn.xh.domain.Book;
import cn.xh.domain.Category;
import cn.xh.domain.User;
import cn.xh.service.ManagerService;
import cn.xh.service.impl.ManagerServiceImpl;

@WebServlet("/manager/ManagerServlet")
public class ManagerServlet extends HttpServlet {
	private ManagerService service = new ManagerServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String op = req.getParameter("op");
		// ����Ա��½
		if (op.equals("login")) {
			login(req, resp);
		}
		// ע��
		if (op.equals("layout")) {
			layout(req, resp);
		}
		// �޸Ĺ���Ա����
		if (op.equals("managerInformation")) {
			managerInformation(req, resp);
		}
		// �޸Ĺ���Ա��¼����
		if (op.equals("managerPassword")) {
			managerPassword(req, resp);
		}
		
		// ����鼮ǰ�Ȼ�ȡ���з���
		if (op.equals("addBookUI")) {
			addBookUI(req, resp);
		}
		// ����鼮
		if (op.equals("addBook")) {
			try {
				addBook(req, resp);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
		// ����鼮����
		if (op.equals("addCategory")) {
			addCategory(req, resp);
		}
		//�����鼮
		if (op.equals("loadAll")) {
			loadAll(req, resp);
		}
		// ��ѧ�������鼮�б�
		if (op.equals("wxys")) {
			wxys(req, resp);
		}
		// ����������鼮�б�
		if (op.equals("rwsk")) {
			rwsk(req, resp);
		}
		// �ٶ�ͯ�����鼮�б�
		if (op.equals("sets")) {
			sets(req, resp);
		}
		// �����������鼮
		if (op.equals("jyks")) {
			jyks(req, resp);
		}
		// ���ý������鼮
		if (op.equals("jjjr")) {
			jjjr(req, resp);
		}
		// ��ѧ�������鼮
		if (op.equals("kxjs")) {
			kxjs(req, resp);
		}
		// ��ѧ�������鼮
		if (op.equals("search")) {
			search(req, resp);
		}
		// �༭�鼮��Ϣǰ��ȡ�鼮����Ϣ����
		if (op.equals("editBookUI")) {
			editBookUI(req, resp);
		}
		// �༭�鼮
		if (op.equals("editBook")) {
			try {
				editBook(req, resp);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
		// ɾ���鼮
		if (op.equals("delBook")) {
			delBook(req, resp);
		}
		// ��ȡ�鼮�����б�
		if (op.equals("categoryList")) {
			categoryList(req, resp);
		}
		// ��÷�����Ϣ
		if (op.equals("editCategoryUI")) {
			editCategoryUI(req, resp);
		}
		// �޸��鼮������Ϣ
		if (op.equals("editCategory")) {
			editCategory(req, resp);
		}
		// ɾ���鼮����
		if (op.equals("delCategory")) {
			delCategory(req, resp);
		}
		// �û���Ϣ����
		if (op.equals("findUsers")) {
			findUsers(req, resp);
		}
		//�û���Ϣ�༭����
		if (op.equals("editUserUI")) {
			editUserUI(req,resp);
		}
		//�û���Ϣ�޸�ʵ��
		if (op.equals("editUser")) {
			editUser(req,resp);
		}
		// �鼮�������
		if (op.equals("sales")) {
			sales(req, resp);
		}
	}

	//��¼ʵ��
	private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		HttpSession session = req.getSession();
		Administrator admin = service.login(username, password);
		if (admin.getUsername() != null && admin.getUsername() != "") {
			req.setAttribute("message1", "��ӭ��½");
			req.setAttribute("message2", "����Ա");
			session.setAttribute("admin", admin);
			req.getRequestDispatcher("/comm/jump.jsp").forward(req, resp);
		} 
		else {
			req.setAttribute("message1", "�˺Ż��������");
			req.getRequestDispatcher("/comm/jump.jsp").forward(req, resp);
		}
	}
		
	//ע��ʵ��
	private void layout(HttpServletRequest req, HttpServletResponse resp) {
		try {
			HttpSession session = req.getSession();
			session.removeAttribute("admin");
			req.setAttribute("message1", "ע���ɹ�");
			req.getRequestDispatcher("/comm/jump.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//�޸ĸ�����Ϣʵ��
	private void managerInformation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String username = req.getParameter("username");
		String name = req.getParameter("name");
		String sex = req.getParameter("sex");
		String tel = req.getParameter("tel");
		service.managerInformation(username, name, sex, tel);
		HttpSession session = req.getSession();
		Administrator admin = (Administrator) session.getAttribute("admin");
		admin.setName(name);
		admin.setSex(sex);
		admin.setTel(tel);
		session.setAttribute("admin", admin);
		resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>�޸ĳɹ���</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
					"				style=\"margin-left:48%\"\r\n" + 
					"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");

	}
		
	//�޸ĵ�¼����ʵ��
	private void managerPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String repassword = req.getParameter("repassword");
		
		if (password.equals(repassword)) {
			service.managerPassword(username, password);
			resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>�޸ĳɹ���</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
						"				style=\"margin-left:48%\"\r\n" + 
						"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");
		}
		else {
			resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>�������벻һ�£��޸�ʧ�ܣ�</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
						"				style=\"margin-left:48%\"\r\n" + 
						"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");
		}
		
	}
	
	//ά���鼮��Ϣҳ�棬��ѯ�����鼮
	private void loadAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Book> books = service.loadAll();
		req.setAttribute("books", books);
		req.getRequestDispatcher("/manager/managerBookInfo.jsp").forward(req, resp);
		
	}
	
	//ά���鼮��Ϣҳ�棬��ѯ��ѧ�������鼮
	private void wxys(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Book> books = service.xsys();
		req.setAttribute("books", books);
		req.getRequestDispatcher("/manager/managerBookInfo.jsp").forward(req, resp);
	}

	//ά���鼮��Ϣҳ�棬��ѯ����������鼮
	private void rwsk(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Book> books = service.rwsk();
		req.setAttribute("books", books);
		req.getRequestDispatcher("/manager/managerBookInfo.jsp").forward(req, resp);
	}
	
	//ά���鼮��Ϣҳ�棬��ѯ�ٶ�ͯ�����鼮
	private void sets(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Book> books = service.sets();
		req.setAttribute("books", books);
		req.getRequestDispatcher("/manager/managerBookInfo.jsp").forward(req, resp);
	}

	//ά���鼮��Ϣҳ�棬��ѯ�����������鼮
	private void jyks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Book> books = service.jyks();
		req.setAttribute("books", books);
		req.getRequestDispatcher("/manager/managerBookInfo.jsp").forward(req, resp);
	}

	//ά���鼮��Ϣҳ�棬��ѯ���ھ������鼮
	private void jjjr(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Book> books = service.jjjr();
		req.setAttribute("books", books);
		req.getRequestDispatcher("/manager/managerBookInfo.jsp").forward(req, resp);
	}
	
	//ά���鼮��Ϣҳ�棬��ѯ��ѧ�������鼮
	private void kxjs(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Book> books = service.kxjs(); 
		req.setAttribute("books", books);
		req.getRequestDispatcher("/manager/managerBookInfo.jsp").forward(req, resp);
	}
	
	//ά���鼮��Ϣҳ�棬�����鼮
	private void search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String m = (String) req.getParameter("search");
		System.out.println(m);
		List<Book> books = service.search(m); 
		req.setAttribute("books", books);
		req.getRequestDispatcher("/manager/managerBookInfo.jsp").forward(req, resp);
	}
	
	//ά���鼮��Ϣҳ�����ҳ�棬�༭�鼮��Ϣ����
	private void editBookUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String book_id = req.getParameter("book_id");
		Book book = findBookById(book_id);
		List<Category> category = service.findAllCategory();
		HashMap map = new HashMap();
		map.put("book", book);
		map.put("category", category);
		req.setAttribute("map", map);
		req.getRequestDispatcher("/manager/editBook.jsp").forward(req, resp);
	}
	
	// ͨ���鼮id�ҵ��鼮��Ϣ
	private Book findBookById(String book_id) {
		return service.findBookById(book_id);
	}

	//�޸��鼮��Ϣ�ľ���ʵ��
	private void editBook(HttpServletRequest req, HttpServletResponse resp) throws FileUploadException, IOException {
		String book_id = req.getParameter("book_id");
		String book_name = req.getParameter("book_name");
		String book_author = req.getParameter("book_author");
		String book_press = req.getParameter("book_press");
		String book_desc = req.getParameter("book_desc");
		double book_price = Double.parseDouble(req.getParameter("book_price"));
		String book_kunumber = req.getParameter("book_kunumber");
		service.editBook(book_id, book_name, book_author, book_press, book_desc, book_price, book_kunumber);
		resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>�޸ĳɹ���</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
				"				style=\"margin-left:48%\"\r\n" + 
				"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");
	}
	
	//ά���鼮��Ϣҳ�����ҳ�棬ɾ���鼮
	private void delBook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String book_id = req.getParameter("book_id");
		service.delBook(book_id);
		resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>ɾ���ɹ���</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
				"				style=\"margin-left:48%\"\r\n" + 
				"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");
	}
	
	//�鼮������Ϣ
	private void categoryList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Category> categories = service.findAllCategory();
		req.setAttribute("categories", categories);
		req.getRequestDispatcher("/manager/managerCategories.jsp").forward(req, resp);
	}

	//�鼮������Ϣ�ӽ���༭������Ϣ
	private void editCategoryUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Category category = service.findCategoryById(req.getParameter("category_id"));
		req.setAttribute("category", category);
		req.getRequestDispatcher("/manager/editCategory.jsp").forward(req, resp);
	}
	//�༭������Ϣʵ��
	private void editCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Category category = new Category(req.getParameter("category_id"), req.getParameter("category_name"),
				req.getParameter("category_desc"));
		service.editCategory(category);
		resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>�޸ĳɹ���</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
				"				style=\"margin-left:48%\"\r\n" + 
				"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");
	}
	//ɾ������
	private void delCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String category_id = req.getParameter("category_id");
		service.delCategory(category_id);
		resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>ɾ���ɹ���</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
				"				style=\"margin-left:48%\"\r\n" + 
				"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");
	}

	//����鼮����
	private void addBookUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Category> categorys = service.findAllCategory();
		req.setAttribute("cs", categorys);
		req.getRequestDispatcher("/manager/managerAddBook.jsp").forward(req, resp);

	}
	
	//����鼮ʵ��
	private void addBook(HttpServletRequest req, HttpServletResponse resp) throws FileUploadException, IOException {
		// �жϱ��ǲ���multipart/form-data����
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (!isMultipart) {
			throw new RuntimeException("���ϴ��������󣡣�");
		}
		// ������������ ������������
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// �������������
		ServletFileUpload sfu = new ServletFileUpload(factory);
		List<FileItem> items = new ArrayList<FileItem>();
		items = sfu.parseRequest(req);
		Book book = new Book();
		for (FileItem item : items) {
			if (item.isFormField()) {
				// ��ͨ�ֶΣ������ݷ�װ��book������
				processFormField(item, req, book);
			} else {
				// �ϴ��ֶΣ��ϴ�
				processUplodFiled(item, req, book);
			}
		}
		// ���鼮��Ϣ���浽���ݿ���
		service.addBook(book);
		resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>��ӳɹ���</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
				"				style=\"margin-left:48%\"\r\n" + 
				"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");
	}

	// �����ļ��ϴ�
	private void processUplodFiled(FileItem item, HttpServletRequest req, Book book) {
		try {
			// ���·������Ҫ����web-inf��
			// 01.��ȡ����ļ�����ʵĿ¼
			/* String dirImages = getServletContext().getRealPath("/images"); */ //��������������²���tomcat��ʧ����ͼƬ
			String dirImages = "C:\\Users\\XY\\J2EE\\BookStore\\WebContent\\images";
			// 02. ͨ��io���ļ�
			// 03. �����ļ��� ���û��ϴ�ͼƬ�� ͼƬ�������ظ���
			String FieldName = item.getFieldName();// ������nameֵ
			String name = item.getName();
			String fileType = name.substring(name.lastIndexOf(".") + 1);
			String fileName = UUID.randomUUID().toString();// �����ò��ظ����ļ���
			// �����ļ�����
			Date time = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strTime = simpleDateFormat.format(time);
			// path����filename
			String path = strTime;// ��ŵ�book������
			String filename = fileName + "." + fileType;
			// fileDir��ͼƬ���մ�����fileDir
			File fileDir = new File(dirImages, path + File.separator + filename);
			// InputStream inputStream = item.getInputStream(); ������ ������ ͨ�����ķ�ʽ
			// �� �ϴ����ļ����ص� �ڴ��� ���������
			File parentDir = new File(dirImages, path);// ��Ŀ¼
			if (!parentDir.exists()) {
				parentDir.mkdirs();
			}
			book.setFilename(filename);
			book.setPath(path);

			InputStream inputStream = item.getInputStream();
			FileOutputStream fos = new FileOutputStream(fileDir);
			int len = 0;
			while ((len = inputStream.read()) != -1) {
				fos.write(len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// ��Fielditem�е����ݷ�װ��book��
	private void processFormField(FileItem item, HttpServletRequest req, Book book) {
		try {
			// itemÿһ��item�������һ�������
			// 01. ��ȡinput������ name ��ֵ ���ݹ淶 ����� ��name��ȡֵ�� �� javabean �е� ������һ��
			String inputName = item.getFieldName();
			String inputValue = item.getString("UTF-8");
			if (inputName.equals("categoryid")) {// ���൥������
				// ��ȡ��ѡ��ķ����id���������idȡ���������Ϣ
				String categoryid = item.getString();
				Category category = service.findCategoryById(categoryid);
				book.setCategory(category);
			} else {
				BeanUtils.setProperty(book, inputName, inputValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ����鼮����
	private void addCategory(HttpServletRequest req, HttpServletResponse resp) {
		try {
			Category category = new Category();
			BeanUtils.populate(category, req.getParameterMap());
			service.addCategory(category);// ������ӷ���
			resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>��ӳɹ���</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
					"				style=\"margin-left:48%\"\r\n" + 
					"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//�û���Ϣ����
	private void findUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<User> users = service.findUsers();
		HttpSession session = req.getSession();
		session.setAttribute("users", users);
		req.getRequestDispatcher("/manager/managerUsers.jsp").forward(req, resp);
	}
	
	//�û���Ϣ�༭����
	private void editUserUI(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user_id = req.getParameter("userId");
		User user = service.findUserById(user_id);
		req.setAttribute("user", user);
		req.getRequestDispatcher("/manager/editUser.jsp").forward(req, resp);
	}
	
	//�û���Ϣ�޸�ʵ��
	private void editUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String username = req.getParameter("username");
		String name = req.getParameter("name");
		String sex = req.getParameter("sex");
		String tel = req.getParameter("tel");
		String address = req.getParameter("address");
		User user = new User(null, username, null, name, sex, tel, address);
		service.editUser(user);
		resp.getWriter().write("<div style='text-align: center;margin-top: 260px'>��ӳɹ���</div><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\"\r\n" + 
				"				style=\"margin-left:48%\"\r\n" + 
				"				onclick=\"window.location='managerContent.jsp'\">�ر�</button>");
	}	
	
	//�鼮����
	private void sales(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Book> sales = service.sales();
		req.setAttribute("sales", sales);
		req.getRequestDispatcher("/manager/managerSales.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
