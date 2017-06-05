
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class File_Compare {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, Exception {
		// TODO Auto-generated method stub
		
		File file = new File("C:/Users/sw33/Desktop/file_original.txt"); // file_original.txt ��� ����
		
		boolean isExists = file.exists(); // file_original.txt ���翩�� �Ǵ�
		
		if(isExists){ // �����ϸ� 
			CFile_Compare();
		}
		else { // �������� ������
			Original_file();
		}
	}

	public static void CFile_Compare() throws FileNotFoundException, IOException, Exception { // file_original.txt ������ ���� ���
		// TODO Auto-generated method stub
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // ���� ���� ������ ����
		
		File dir_home = new File("C:/Users/sw33/Desktop/test/"); // ���� ���� ��� 1��
		if(dir_home.exists() == false){
			System.out.println("Not exist");
			return;
		}
		File dir_log = new File("C:/Users/sw33/Desktop/test1/"); // ���� ���� ��� 2��
		if(dir_log.exists() == false){
			System.out.println("Not exist");
			return;
		}
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/sw33/Desktop/file_change.txt"))); // �ٲ� ��� ���� ���
		BufferedWriter rs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/sw33/Desktop/file_result.txt"))); // ��� ��� ���� ���
		
		ArrayList<File> home_flist = new ArrayList<File>(); // 1�� ����(dir_home) Ž���� ���� ������ ����Ʈ
		ArrayList<File> log_flist = new ArrayList<File>(); // 2�� ���� Ž����(dir_log) ���� ������ ����Ʈ
		
		visitAllFiles(home_flist, dir_home); // 1��(dir_home) ���� Ž��
		visitAllFiles(log_flist, dir_log); // 2��(dir_log) ���� Ž��
		
		for(File f : home_flist){ // 1�� Ž���� ���� �ϳ��� ��� ����(file_change.txt)�� write
			String line = f.getName() + "/" + f.length() + "/" + sdf.format(new Date(f.lastModified())); // �����̸�, ����ũ��, ��������������¥ line�� ����
			bw.write(line);
			bw.newLine(); // �� �ٲ�
		}
		bw.write("==================================================================================================="); // ���� ���� �޶����� ��� ǥ��
		bw.newLine();
		
		for(File f2 : log_flist){ // 2�� Ž���� ���� �ϳ��� ��� ����(file_change.txt)�� write
			String line = f2.getName() + "/" + f2.length() + "/" + sdf.format(new Date(f2.lastModified())); // �����̸�, ����ũ��, ��������������¥ line�� ����
			bw.write(line);		
			bw.newLine();
		}
		bw.flush(); // ���۰� ���� �ʾƵ� ������
		bw.close();
		
		BufferedReader origin_file = new BufferedReader(new FileReader("C:/Users/sw33/Desktop/file_original.txt/")); // ���ϱ����� file_original.txt �о�帲
		BufferedReader chan_file = new BufferedReader(new FileReader("C:/Users/sw33/Desktop/file_change.txt/")); // ���ϱ����� file_change.txt �о�帲
		
		try {
			String origin_line = ""; // origin_file(file_original.txt) split �Լ� �������� String���� ��ȯ
			String chan_line = ""; // chan_file(file_change.txt) split �Լ� �������� String���� ��ȯ
			
			ArrayList<String> origin_list = new ArrayList<String>(); // file_original.txt�� ������ �迭
			ArrayList<String> chan_list = new ArrayList<String>(); // file_change.txt�� ������ �迭
			
			ArrayList<String> origin_f_list = new ArrayList<String>(); // file_original.txt���� ���� �̸��� ���� ������ �迭
			ArrayList<String> chan_f_list = new ArrayList<String>(); // file_change.txt���� ���� �̸��� ���� ������ �迭
			
			while((origin_line = origin_file.readLine()) != null) { // file_original.txt�� origin_list�� �ϳ��� ���� & file_original.txt�� �����̸� origin_f_list�� �ϳ��� ����
				String[] f_o_attribute = origin_line.split("=", -1);
				String[] f_o_fname = origin_line.split("/", -1);
				origin_list.add(f_o_attribute[0]);
				origin_f_list.add(f_o_fname[0]);
			}
			while((chan_line = chan_file.readLine()) != null) { // file_change.txt�� chan_list�� �ϳ��� ����
				String[] f_c_attribute = chan_line.split("=", -1);
				chan_list.add(f_c_attribute[0]);
			}

			rs.write("�߰�/����");
			rs.newLine();
			rs.write("�ٲ����ϼӼ� \t\t\t\t\t ó�����ϼӼ�");
			rs.newLine();
			
			for(int i = 0; i < chan_list.size(); i++) { // file_change.txt���� �ִ� ����(���� �����ǰų� ���������� ������ ���)
				boolean aa = false;
				for(int j = 0; j < origin_list.size(); j++) {
					if(chan_list.get(i).equals(origin_list.get(j))) {
						aa = true;
						break;
					}
				}
				if(!aa) { // file_original.txt�� ���� ��츸 ����
					rs.write(chan_list.get(i));
					rs.flush();
					rs.close();
					
					chan_line = chan_list.get(i); // file_change.txt ������  String���� ��ȯ
	                String[] f_c_fname = chan_line.split("/", -1); // '/'�� �������� �߶� �ϳ��� f_c_fname�� ���� -> file_change.txt ������ ���� �̸��� �����ϱ� ����
	                chan_f_list.add(f_c_fname[0]); // file_change.txt ������ �����̸��� lis4�� ����

					rs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/sw33/Desktop/file_result.txt", true))); // rs �ٽ� ���鼭 ����� ����
					for(int z = 0; z < origin_f_list.size(); z++) { // origin_f_list = chan_f_list �� �����̸��� ������� origin_list(file_original.txt�� �����̸� ���)
						if(chan_f_list.contains(origin_f_list.get(z))) {
							rs.write("\t\t" + origin_list.get(z));
						}
					}
					chan_f_list.clear(); // ������� ������ ��� �����Ͽ� chan_f_list�� ���̱� ������ �̹� �����̸��� ���� ������ ã�� �� ��� �����ؼ� ��µ�
					rs.newLine();
				}
			}
			rs.write("==================================================================================================="); // ����� ��� ǥ��(�߰� ===== ����)
			rs.newLine();
			rs.write("����");
			rs.newLine();
			
			for(int i = 0; i < origin_list.size(); i++) { // file_original.txt���� �ִ� ����
				boolean aa = false;
				for(int j = 0; j < chan_list.size(); j++) {
					if(origin_list.get(i).equals(chan_list.get(j))) {
						aa = true;
						break;
					}
				}
				if(!aa) {
					rs.write(origin_list.get(i));
					rs.newLine();
				}
			}			
			rs.flush();
			rs.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			origin_file.close();
			chan_file.close();
		}
	}

	public static void Original_file() throws FileNotFoundException, IOException, InterruptedException { // file_original.txt ������ ���� ���
		// TODO Auto-generated method stub
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // ���� ���� ������ ����
		
		File dir_home = new File("C:/Users/sw33/Desktop/test"); // ���� ���� ��� 1��
		if(dir_home.exists() == false){
			System.out.println("Not exist");
			return;
		}
		File dir_log = new File("C:/Users/sw33/Desktop/test1/"); // ���� ���� ��� 2��
		if(dir_log.exists() == false){
			System.out.println("Not exist");
			return;
		}
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/sw33/Desktop/file_original.txt"))); // ���� ��� ���� ���
		
		ArrayList<File> home_flist = new ArrayList<File>(); // 1�� ����(dir_home) Ž���� ���� ������ ����Ʈ
		ArrayList<File> log_flist = new ArrayList<File>(); // 2�� ����(dir_log) Ž���� ���� ������ ����Ʈ
		
		visitAllFiles(home_flist, dir_home); // 1��(dir_home) ���� Ž��
		visitAllFiles(log_flist, dir_log); // 2��(dir_log) ���� Ž��
		
		for(File f : home_flist){ // 1�� Ž���� ���� �ϳ��� ��� ����(file_original.txt)�� write
			String line = f.getName() + "/" + f.length() + "/" + sdf.format(new Date(f.lastModified()));
			bw.write(line);
			bw.newLine();			
		}
		bw.write("===================================================================================================");
		bw.newLine();
		
		for(File f2 : log_flist){ // 2�� Ž���� ���� �ϳ��� ��� ����(file_original.txt)�� write
			String line2 = f2.getName() + "/" + f2.length() + "/" + sdf.format(new Date(f2.lastModified()));
			bw.write(line2);
			bw.newLine();			
		}
		bw.flush();
		bw.close();
	}

	public static void visitAllFiles(ArrayList<File> files, File dir){ // ���� �� ���� ���� Ž��
		if(dir.isDirectory()){
			File[] children = dir.listFiles();
			for(File f : children){
				visitAllFiles(files,f); // ���� ���� Ž��
			}
		}
		else{
			files.add(dir);
		}
	}

}
