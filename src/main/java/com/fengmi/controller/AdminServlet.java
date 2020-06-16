package com.fengmi.controller;

import java.util.Date;
import java.util.UUID;

import javax.management.Query;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.fengmi.entity.QueryCondition;
import org.apache.commons.beanutils.BeanUtils;

import com.fengmi.entity.Admin;
import com.fengmi.entity.Goods;
import com.fengmi.entity.PageBean;
import com.fengmi.service.AdminService;
import com.fengmi.service.GoodsService;
import com.fengmi.service.impl.AdminServiceImpl;
import com.fengmi.service.impl.GoodsServiceImpl;

/**
 * ��̨����ϵͳ - ����
 * @author Administrator
 */
@WebServlet("/admin")
@MultipartConfig
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private AdminService adminService = new AdminServiceImpl();
	private GoodsService goodsService = new GoodsServiceImpl();
	
	/**
	 * ��½���� login
	 * @param request
	 * @param response
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("login...");
		
		Admin admin = new Admin();
		try {
			BeanUtils.populate(admin, request.getParameterMap());
			if (null != adminService.login(admin)) {
				request.getSession().setAttribute("admin", admin);
				return "redirect:/admin?methodName=toGetGoodsList";
			} else {
				request.setAttribute("errMsg", "�˺Ż�������������ԣ�");
				return "/after/login.jsp";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/after/login.jsp";
	}
	
	/**
	 * �ǳ����� logout
	 * @param request
	 * @param response
	 */
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("logout...");
		
		request.getSession().invalidate();
		return "redirect:/after/login.jsp";
	}

	/**
	 * ��ҳ��ʾ���� ��ת goods_list.jsp
	 * @param request
	 * @param response
	 * @return
	 */
	public String toGetGoodsList(HttpServletRequest request, HttpServletResponse response) {
		String currentPageStr = request.getParameter("currentPage");
		Integer currentPageNo = currentPageStr == null ? 1 : Integer.parseInt(currentPageStr);
		
		try {
			PageBean<Goods> pageBean = goodsService.getAllGoodsByPageNo(currentPageNo);
			request.setAttribute("pageBean", pageBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/after/goods_list.jsp";
	}

	/**
	 * ��� / �޸ķ��� saveOrUpdate
	 * @param request
	 * @param response
	 * @return
	 */
	public String saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
		Goods goods = new Goods();
		String id = request.getParameter("id"); // ""
		
		try {
			// �������� ���/�޸� ʱ��
			goods.setDeployDate(new Date());
			// �������� ͼƬ·��
			Part part = request.getPart("imgPath");
			String imgPath = UUID.randomUUID().toString().replace("-", "") + "-" + part.getSubmittedFileName();
			part.write("D:\\tomcat\\apache-tomcat-9.0.35-fileServer\\webapps\\uploadfile\\images\\" + imgPath);
			goods.setImgPath(imgPath);
			// ������ ��װ��������
			BeanUtils.populate(goods, request.getParameterMap());
			System.out.println("�µģ�" + goods);

			if (null == id || "".equals(id)) {
				// id Ϊ""��ʱ�����
				if (!goodsService.saveOneGoods(goods)) {
					// ���ʧ��
					request.setAttribute("errMsg", "δ֪�������ʧ�ܣ�");
					return "/after/add_goods.jsp";
				}
				// ��ӳɹ���nothing to do
			} else {
				// id ����ʱ���޸�
				if (!goodsService.updateOneGoods(goods)) {
					// �޸�ʧ��
					request.setAttribute("errMsg", "δ֪�����޸�ʧ�ܣ�");
					return "/after/add_goods.jsp";
				}
				// �޸ĳɹ���nothing to do
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/admin?methodName=toGetGoodsList";
	}

	/**
	 * ���� id ��ȡ��Ʒ��Ϣ
	 * @param request
	 * @param response
	 * @return
	 */
	public String getGoodsById(HttpServletRequest request, HttpServletResponse response) {
		Integer id = Integer.parseInt(request.getParameter("id"));
		try {
			Goods goods = goodsService.getGoodsById(id);
			System.out.println("�޸ģ�" + goods);
			request.setAttribute("goods", goods);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/after/add_goods.jsp";
	}

	/**
	 * ͨ�� id ɾ��1����Ʒ
	 * @param request
	 * @param response
	 * @return
	 */
	public String delGoodsById(HttpServletRequest request, HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));

		try {
			boolean result = goodsService.delGoodsById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/admin?methodName=toGetGoodsList";
	}

	/**
	 * ���� ���� ��ѯ��Ʒ������ҳ
	 * @param request
	 * @param response
	 * @return
	 */
	public String queryGoodsByCondition(HttpServletRequest request, HttpServletResponse response) {
		// ��һ�ν������ ��ô��ȡ���� pageNo������ֵ Ӧ���� null
		String currentPageStr = request.getParameter("currentPage");
		if (null == currentPageStr) {
			currentPageStr = "1";
			request.getSession().setAttribute("condition", null);
		}

		String inputGoodsName = request.getParameter("inputGoodsName");
		String typeId = request.getParameter("typeId");
		QueryCondition qc = new QueryCondition();
		QueryCondition condition = (QueryCondition) request.getSession().getAttribute("condition");
		// ������벻Ϊ�գ�ʹ��������������� session ��Ϊ�գ���ʹ����������
		if ((null != inputGoodsName && !"".equals(inputGoodsName))
			|| (null != typeId && !"".equals(typeId))) {
			qc.setInputGoodsName(inputGoodsName);
			qc.setTypeId(typeId);
		} else if(!"".equals(condition.getInputGoodsName()) || !"".equals(condition.getTypeId())) {
			qc = condition;
		}
		System.out.println("��ѯ������" + qc);

		try {
			PageBean<Goods> pageBean = goodsService.queryGoodsByCondition(Integer.parseInt(currentPageStr), qc);
			request.setAttribute("pageBean", pageBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/after/goods_list.jsp";
	}
}
