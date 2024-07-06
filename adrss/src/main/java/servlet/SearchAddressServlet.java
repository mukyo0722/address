package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class SearchAddressServlet
 */
@WebServlet("/SearchAddressServlet")
public class SearchAddressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchAddressServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String zipcode = request.getParameter("zipcode");
        String address = getAddressFromZipcode(zipcode);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(address);
    }

    private String getAddressFromZipcode(String zipcode) {
        String apiUrl = "http://zipcloud.ibsnet.co.jp/api/search?zipcode=" + zipcode;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            JSONObject json = new JSONObject(result.toString());
            if (json.getInt("status") == 200) {
                JSONArray results = json.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject addressData = results.getJSONObject(0);
                    String address = addressData.getString("address1") + addressData.getString("address2") + addressData.getString("address3");
                    return address;
                } else {
                    return "住所が見つかりませんでした";
                }
            } else {
                return "エラーが発生しました: " + json.getString("message");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "住所の取得に失敗しました";
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
