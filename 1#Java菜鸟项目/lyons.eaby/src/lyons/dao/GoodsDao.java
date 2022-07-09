package lyons.dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.rowset.CachedRowSetImpl;

import lyons.db.DbClose;
import lyons.db.DbConn;
import lyons.entity.Goods;
import lyons.entity.Login;

public class GoodsDao extends HttpServlet
{
	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 135785434567L;
 
	/**
	 * Constructor of the object.
	 */
	public GoodsDao()
	{
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy()
	{
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		response.setContentType("text/html;chartset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		String value = "";
		value = request.getParameter("key");
		int key = Integer.parseInt(value);
		System.out.println("����Ƿ���key:"+key);
		
		String keyWord = "";
		keyWord = request.getParameter("keyWord");
		System.out.println(keyWord);
		queryGoods(request, response, key,keyWord);
	}

	public void init() throws ServletException
	{
		// Put your code here
	}
	
	/**
	 * ��Ʒ��ѯ
	 * @param request
	 * @param response
	 * @param key ��ѯ������/int:4(�򵥲�ѯ)
	 * @return ��Ʒ��Ϣ����
	 * @throws ServletException
	 * @throws IOException
	 */
	public void queryGoods(HttpServletRequest request, HttpServletResponse response,int key,String keyWord)
			throws ServletException, IOException
	{
	    response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        CachedRowSetImpl rowSet = null;//�м�����
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Goods goods = null;
		Login username = null;
//		OrderForm orderForm = null;
		
		HttpSession session = request.getSession(true);
		username = (Login)session.getAttribute("loginBean");
		goods = (Goods)session.getAttribute("goods");
//		orderForm = (OrderForm)session.getAttribute("orderForm");
//		ArrayList<Goods> goodsList = new ArrayList<Goods>();
		if (goods==null)
		{
			goods = new Goods();
			session.setAttribute("goods", goods);
		}
		if (username==null)
		{
		    username = new Login();
		    session.setAttribute("username", username);
		}
//		if (orderForm==null)
//		{
//		    orderForm = new OrderForm();
//		    session.setAttribute("orderForm", orderForm);
//		}
		  //�ж��û��Ƿ��½
		  String user = "";
          user = username.getUsername();//��½�ߵ��û���
          System.out.println("�����û���"+user);
          if (user.equals("userNull"))
          {
              out.print("<br>");
              out.print("<center><font color=#008B8B> ��½֮����ܿ�����Ŷ  </font>");
              out.print("<a href=/lyons.eaby/jsp/join/login.jsp><font color=red size=6>��½</font></a></center>");
              return;
          }
		
		conn = DbConn.getConn();	

		switch (key)
		{
			case 1:
					/*//	key=1��Ʒ ���� �����ѯ
					String sqlGnum = "SELECT * FROM GOODS ORDER BY GNUM ASC";
					try
					{
						pstmt = conn.prepareStatement(sqlGnum);
						rs = pstmt.executeQuery();
						while (rs.next())
						{
							
						}
					} catch (SQLException e)
					{
						e.printStackTrace();
					}finally
							{
								DbClose.allClose(pstmt, rs, conn);
							}*/
				break;
			case 2:
        			  //key=2 ���չؼ��ֲ�ѯ ��Ʒ��Ϣ
                      
                        String sqlShowGoodsByKey =  
                        "select * from commodity WHERE commodity_name LIKE '%'||?||'%'";
                        try
                        {
                            pstmt = conn.prepareStatement(sqlShowGoodsByKey);
                            pstmt.setString(1, keyWord);
                            rs = pstmt.executeQuery();
                            System.out.println("--2�鿴����ִ�����ݿ����--");
                            if(rs.next())
                            {
                                rs = pstmt.executeQuery();//���²�ѯ��ԭ����rs.nextʱ���ƫ�ƺ󣬶�����¼��
                                rowSet = new CachedRowSetImpl();
                                rowSet.populate(rs); 
                                goods.setRowSet(rowSet);
                                System.out.println("2�Ѿ������ݿ��л�ȡ��ֵ���������м�");
                                request.getRequestDispatcher("/jsp/browse/showGoods.jsp").forward(request, response);
                            }else 
                                {
                                    out.print("<br><br><br><center>");
                                    out.print("<font color=green> ��,��ѯ������.�����ؼ����ٴ� </font>");
                                    out.print("<a href=/lyons.eaby/jsp/browse/searchByKeyWord.jsp><font color=red size=6>��ѯ</font></a>");
                                    out.print("</center>");     
                                }
                        } catch (SQLException e)
                        {
                            System.out.println("key=3�鿴�����쳣��"+e);
                            
                        }finally
                                {
                                    System.out.println("�鿴����ִ�йر���");
                                    DbClose.allClose(pstmt, rs, conn);
                                }
        				break;
			case 3:
                    //key=3 ���յ�¼�˲�ѯ���� ��Ʒ����+����
			      
                    String sqlOrder= 
                    "select commodity_name,sum(sum) from orderform where username=? group by commodity_name having sum(sum)>0";
                    try
                    {
                        pstmt = conn.prepareStatement(sqlOrder);
                        pstmt.setString(1, user);
                        rs = pstmt.executeQuery();
                        System.out.println("--�鿴����ִ�����ݿ����--");
                        if(rs.next())
                        {
                            rs = pstmt.executeQuery();//���²�ѯ��ԭ����rs.nextʱ���ƫ�ƺ󣬶�����¼��
                            rowSet = new CachedRowSetImpl();
                            rowSet.populate(rs); 
                            goods.setRowSet(rowSet);
                            System.out.println("3�Ѿ������ݿ��л�ȡ��ֵ���������м�");
                            request.getRequestDispatcher("/jsp/order/lookOrderForm.jsp").forward(request, response);
                        }else 
                            {
                                out.print("<br><br><br><center>");
                                out.print("<font color=green> ��,�����ǿյ��� </font>");
                                out.print("<a href=/lyons.eaby/lyons.dao/GoodsDao?key=4><font color=red size=6>Go Shopping</font></a>");
                                out.print("</center>");		
                            }
                    } catch (SQLException e)
                    {
                        System.out.println("key=3�鿴�����쳣��"+e);
                        
                    }finally
                            {
                                System.out.println("�鿴����ִ�йر���");
                                DbClose.allClose(pstmt, rs, conn);
                            }
                    break;
			case 4:
			        StringBuffer url = request.getRequestURL();
			        System.out.println("4324234=========="+url.toString());
					//key=4 �����Ʒ
					String sqlList= "select * from commodity";
					try
					{
						pstmt = conn.prepareStatement(sqlList);
						rs = pstmt.executeQuery();
						System.out.println("--4�����Ʒִ�����ݿ����--");
						if(rs.next())
						{
						    rs = pstmt.executeQuery();//���²�ѯ��ԭ����rs.nextʱ���ƫ�ƺ󣬶�����¼��
							rowSet = new CachedRowSetImpl();
							rowSet.populate(rs);
							goods.setRowSet(rowSet);
							System.out.println("4�����Ʒ�Ѿ������ݿ��л�ȡ��ֵ���������м�");
							request.getRequestDispatcher("/jsp/browse/showGoods.jsp").forward(request, response);
						}else 
                        {
                                out.print("<br><br><br><center>");
                                out.print("<font color=green> ��,���һ�û�ϻ��� </font>");
                                out.print("<a href=/lyons.eaby/lyons.dao/GoodsDao?key=4><font color=red size=6>������ҳ</font></a>");
                                out.print("</center>");     
                            }
					} catch (SQLException e)
					{
						e.printStackTrace();
						response.sendRedirect("/lyons.eaby/jsp/browse/showGoods.jsp");
					}finally
							{
								DbClose.allClose(pstmt, rs, conn);
							}
					break;
			default:
				break;
		}
	}

}