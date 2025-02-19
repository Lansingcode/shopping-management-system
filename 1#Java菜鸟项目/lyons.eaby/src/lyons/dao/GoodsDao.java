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
		System.out.println("????????key:"+key);
		
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
	 * ??????
	 * @param request
	 * @param response
	 * @param key ?????????/int:4(????)
	 * @return ??????????
	 * @throws ServletException
	 * @throws IOException
	 */
	public void queryGoods(HttpServletRequest request, HttpServletResponse response,int key,String keyWord)
			throws ServletException, IOException
	{
	    response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        CachedRowSetImpl rowSet = null;//?м?????
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
		  //?ж?????????
		  String user = "";
          user = username.getUsername();//???????????
          System.out.println("?????????"+user);
          if (user.equals("userNull"))
          {
              out.print("<br>");
              out.print("<center><font color=#008B8B> ???????????????  </font>");
              out.print("<a href=/lyons.eaby/jsp/join/login.jsp><font color=red size=6>???</font></a></center>");
              return;
          }
		
		conn = DbConn.getConn();	

		switch (key)
		{
			case 1:
					/*//	key=1??? ???? ??????
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
        			  //key=2 ?????????? ??????
                      
                        String sqlShowGoodsByKey =  
                        "select * from commodity WHERE commodity_name LIKE '%'||?||'%'";
                        try
                        {
                            pstmt = conn.prepareStatement(sqlShowGoodsByKey);
                            pstmt.setString(1, keyWord);
                            rs = pstmt.executeQuery();
                            System.out.println("--2?????????????????--");
                            if(rs.next())
                            {
                                rs = pstmt.executeQuery();//?????????????rs.next????????????????
                                rowSet = new CachedRowSetImpl();
                                rowSet.populate(rs); 
                                goods.setRowSet(rowSet);
                                System.out.println("2???????????л??????????????м?");
                                request.getRequestDispatcher("/jsp/browse/showGoods.jsp").forward(request, response);
                            }else 
                                {
                                    out.print("<br><br><br><center>");
                                    out.print("<font color=green> ??,?????????.???????????? </font>");
                                    out.print("<a href=/lyons.eaby/jsp/browse/searchByKeyWord.jsp><font color=red size=6>???</font></a>");
                                    out.print("</center>");     
                                }
                        } catch (SQLException e)
                        {
                            System.out.println("key=3??????????"+e);
                            
                        }finally
                                {
                                    System.out.println("????????й????");
                                    DbClose.allClose(pstmt, rs, conn);
                                }
        				break;
			case 3:
                    //key=3 ?????????????? ???????+????
			      
                    String sqlOrder= 
                    "select commodity_name,sum(sum) from orderform where username=? group by commodity_name having sum(sum)>0";
                    try
                    {
                        pstmt = conn.prepareStatement(sqlOrder);
                        pstmt.setString(1, user);
                        rs = pstmt.executeQuery();
                        System.out.println("--?????????????????--");
                        if(rs.next())
                        {
                            rs = pstmt.executeQuery();//?????????????rs.next????????????????
                            rowSet = new CachedRowSetImpl();
                            rowSet.populate(rs); 
                            goods.setRowSet(rowSet);
                            System.out.println("3???????????л??????????????м?");
                            request.getRequestDispatcher("/jsp/order/lookOrderForm.jsp").forward(request, response);
                        }else 
                            {
                                out.print("<br><br><br><center>");
                                out.print("<font color=green> ??,?????????? </font>");
                                out.print("<a href=/lyons.eaby/lyons.dao/GoodsDao?key=4><font color=red size=6>Go Shopping</font></a>");
                                out.print("</center>");		
                            }
                    } catch (SQLException e)
                    {
                        System.out.println("key=3??????????"+e);
                        
                    }finally
                            {
                                System.out.println("????????й????");
                                DbClose.allClose(pstmt, rs, conn);
                            }
                    break;
			case 4:
			        StringBuffer url = request.getRequestURL();
			        System.out.println("4324234=========="+url.toString());
					//key=4 ??????
					String sqlList= "select * from commodity";
					try
					{
						pstmt = conn.prepareStatement(sqlList);
						rs = pstmt.executeQuery();
						System.out.println("--4?????????????????--");
						if(rs.next())
						{
						    rs = pstmt.executeQuery();//?????????????rs.next????????????????
							rowSet = new CachedRowSetImpl();
							rowSet.populate(rs);
							goods.setRowSet(rowSet);
							System.out.println("4?????????????????л??????????????м?");
							request.getRequestDispatcher("/jsp/browse/showGoods.jsp").forward(request, response);
						}else 
                        {
                                out.print("<br><br><br><center>");
                                out.print("<font color=green> ??,??????????? </font>");
                                out.print("<a href=/lyons.eaby/lyons.dao/GoodsDao?key=4><font color=red size=6>???????</font></a>");
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