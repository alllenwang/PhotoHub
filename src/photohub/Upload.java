package photohub;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;





public class Upload extends HttpServlet {
	@SuppressWarnings("unchecked")
	private static FileItem createFileItem(String filePath)
    {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
            "MyFileName" + extFile);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try
        {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        }
        catch (IOException e)
        {
           
        }

        return item;

    }
	public static boolean UpLoadPhoto(String realFilePath, String FileName)
	{
		
		// Ϊ�������ṩ������Ϣ
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// �����������ʵ��
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// ��ʼ����
		sfu.setFileSizeMax(1024 * 400);
		// ÿ�����������ݻ��װ��һ����Ӧ��FileItem������
		try {
				String prefix = realFilePath.substring(realFilePath.lastIndexOf(".") + 1);
				FileItem item = createFileItem(realFilePath);
			
				// isFormFieldΪtrue����ʾ�ⲻ���ļ��ϴ�����
				
					
					// ��ô���ļ�������·��
					// upload�µ�ĳ���ļ��� �õ���ǰ���ߵ��û� �ҵ���Ӧ���ļ���

					String path = "photo";
						
					System.out.println(path);
					// ����ļ���
					String fileName = item.getName();
					System.out.println(fileName);
					// �÷�����ĳЩƽ̨(����ϵͳ),�᷵��·��+�ļ���
					fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
					File file = new File(path + "\\" + FileName + "." + prefix);
					if (!file.exists()) {
						item.write(file);
						// ���ϴ�ͼƬ�����ּ�¼�����ݿ���

						//resp.sendRedirect("/upload/ok.html");
						
						System.out.println("successful");
						return true;
					}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

}
