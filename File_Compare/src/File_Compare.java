
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class File_Compare {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, Exception {
		// TODO Auto-generated method stub
		
		File file = new File("C:/Users/sw33/Desktop/file_original.txt"); // file_original.txt 경로 설정
		
		boolean isExists = file.exists(); // file_original.txt 존재여부 판단
		
		if(isExists){ // 존재하면 
			CFile_Compare();
		}
		else { // 존재하지 않으면
			Original_file();
		}
	}

	public static void CFile_Compare() throws FileNotFoundException, IOException, Exception { // file_original.txt 파일이 있을 경우
		// TODO Auto-generated method stub
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 파일 최종 수정일 형식
		
		File dir_home = new File("C:/Users/sw33/Desktop/test/"); // 읽을 폴더 경로 1번
		if(dir_home.exists() == false){
			System.out.println("Not exist");
			return;
		}
		File dir_log = new File("C:/Users/sw33/Desktop/test1/"); // 읽을 폴더 경로 2번
		if(dir_log.exists() == false){
			System.out.println("Not exist");
			return;
		}
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/sw33/Desktop/file_change.txt"))); // 바뀐 출력 파일 경로
		BufferedWriter rs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/sw33/Desktop/file_result.txt"))); // 결과 출력 파일 경로
		
		ArrayList<File> home_flist = new ArrayList<File>(); // 1번 파일(dir_home) 탐색한 파일 저장할 리스트
		ArrayList<File> log_flist = new ArrayList<File>(); // 2번 파일 탐색한(dir_log) 파일 저장할 리스트
		
		visitAllFiles(home_flist, dir_home); // 1번(dir_home) 파일 탐색
		visitAllFiles(log_flist, dir_log); // 2번(dir_log) 파일 탐색
		
		for(File f : home_flist){ // 1번 탐색한 파일 하나씩 출력 파일(file_change.txt)에 write
			String line = f.getName() + "/" + f.length() + "/" + sdf.format(new Date(f.lastModified())); // 파일이름, 파일크기, 파일최종수정날짜 line에 저장
			bw.write(line);
			bw.newLine(); // 줄 바꿈
		}
		bw.write("==================================================================================================="); // 읽은 폴더 달라지는 경계 표시
		bw.newLine();
		
		for(File f2 : log_flist){ // 2번 탐색한 파일 하나씩 출력 파일(file_change.txt)에 write
			String line = f2.getName() + "/" + f2.length() + "/" + sdf.format(new Date(f2.lastModified())); // 파일이름, 파일크기, 파일최종수정날짜 line에 저장
			bw.write(line);		
			bw.newLine();
		}
		bw.flush(); // 버퍼가 차지 않아도 쓰게함
		bw.close();
		
		BufferedReader origin_file = new BufferedReader(new FileReader("C:/Users/sw33/Desktop/file_original.txt/")); // 비교하기위해 file_original.txt 읽어드림
		BufferedReader chan_file = new BufferedReader(new FileReader("C:/Users/sw33/Desktop/file_change.txt/")); // 비교하기위해 file_change.txt 읽어드림
		
		try {
			String origin_line = ""; // origin_file(file_original.txt) split 함수 쓰기위해 String으로 변환
			String chan_line = ""; // chan_file(file_change.txt) split 함수 쓰기위해 String으로 변환
			
			ArrayList<String> origin_list = new ArrayList<String>(); // file_original.txt값 저장할 배열
			ArrayList<String> chan_list = new ArrayList<String>(); // file_change.txt값 저장할 배열
			
			ArrayList<String> origin_f_list = new ArrayList<String>(); // file_original.txt에서 파일 이름만 따로 저장할 배열
			ArrayList<String> chan_f_list = new ArrayList<String>(); // file_change.txt에서 파일 이름만 따로 저장할 배열
			
			while((origin_line = origin_file.readLine()) != null) { // file_original.txt값 origin_list에 하나씩 저장 & file_original.txt값 파일이름 origin_f_list에 하나씩 저장
				String[] f_o_attribute = origin_line.split("=", -1);
				String[] f_o_fname = origin_line.split("/", -1);
				origin_list.add(f_o_attribute[0]);
				origin_f_list.add(f_o_fname[0]);
			}
			while((chan_line = chan_file.readLine()) != null) { // file_change.txt값 chan_list에 하나씩 저장
				String[] f_c_attribute = chan_line.split("=", -1);
				chan_list.add(f_c_attribute[0]);
			}

			rs.write("추가/수정");
			rs.newLine();
			rs.write("바뀐파일속성 \t\t\t\t\t 처음파일속성");
			rs.newLine();
			
			for(int i = 0; i < chan_list.size(); i++) { // file_change.txt에만 있는 파일(새로 생성되거나 기존파일이 수정된 경우)
				boolean aa = false;
				for(int j = 0; j < origin_list.size(); j++) {
					if(chan_list.get(i).equals(origin_list.get(j))) {
						aa = true;
						break;
					}
				}
				if(!aa) { // file_original.txt에 없는 경우만 실행
					rs.write(chan_list.get(i));
					rs.flush();
					rs.close();
					
					chan_line = chan_list.get(i); // file_change.txt 내용을  String으로 변환
	                String[] f_c_fname = chan_line.split("/", -1); // '/'를 기준으로 잘라서 하나씩 f_c_fname에 저장 -> file_change.txt 내용의 파일 이름만 추출하기 위해
	                chan_f_list.add(f_c_fname[0]); // file_change.txt 내용의 파일이름만 lis4에 저장

					rs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/sw33/Desktop/file_result.txt", true))); // rs 다시 열면서 덮어쓰기 안함
					for(int z = 0; z < origin_f_list.size(); z++) { // origin_f_list = chan_f_list 의 파일이름이 같을경우 origin_list(file_original.txt의 같은이름 출력)
						if(chan_f_list.contains(origin_f_list.get(z))) {
							rs.write("\t\t" + origin_list.get(z));
						}
					}
					chan_f_list.clear(); // 비워주지 않으면 계속 누적하여 chan_f_list에 쌓이기 때문에 이미 파일이름이 같은 파일을 찾은 후 계속 누적해서 출력됨
					rs.newLine();
				}
			}
			rs.write("==================================================================================================="); // 결과값 경계 표시(추가 ===== 삭제)
			rs.newLine();
			rs.write("삭제");
			rs.newLine();
			
			for(int i = 0; i < origin_list.size(); i++) { // file_original.txt에만 있는 파일
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

	public static void Original_file() throws FileNotFoundException, IOException, InterruptedException { // file_original.txt 파일이 없을 경우
		// TODO Auto-generated method stub
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 파일 최종 수정일 형식
		
		File dir_home = new File("C:/Users/sw33/Desktop/test"); // 읽을 폴더 경로 1번
		if(dir_home.exists() == false){
			System.out.println("Not exist");
			return;
		}
		File dir_log = new File("C:/Users/sw33/Desktop/test1/"); // 읽을 폴더 경로 2번
		if(dir_log.exists() == false){
			System.out.println("Not exist");
			return;
		}
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/sw33/Desktop/file_original.txt"))); // 원본 출력 파일 경로
		
		ArrayList<File> home_flist = new ArrayList<File>(); // 1번 파일(dir_home) 탐색한 파일 저장할 리스트
		ArrayList<File> log_flist = new ArrayList<File>(); // 2번 파일(dir_log) 탐색한 파일 저장할 리스트
		
		visitAllFiles(home_flist, dir_home); // 1번(dir_home) 파일 탐색
		visitAllFiles(log_flist, dir_log); // 2번(dir_log) 파일 탐색
		
		for(File f : home_flist){ // 1번 탐색한 파일 하나씩 출력 파일(file_original.txt)에 write
			String line = f.getName() + "/" + f.length() + "/" + sdf.format(new Date(f.lastModified()));
			bw.write(line);
			bw.newLine();			
		}
		bw.write("===================================================================================================");
		bw.newLine();
		
		for(File f2 : log_flist){ // 2번 탐색한 파일 하나씩 출력 파일(file_original.txt)에 write
			String line2 = f2.getName() + "/" + f2.length() + "/" + sdf.format(new Date(f2.lastModified()));
			bw.write(line2);
			bw.newLine();			
		}
		bw.flush();
		bw.close();
	}

	public static void visitAllFiles(ArrayList<File> files, File dir){ // 폴더 내 하위 폴더 탐색
		if(dir.isDirectory()){
			File[] children = dir.listFiles();
			for(File f : children){
				visitAllFiles(files,f); // 하위 폴더 탐색
			}
		}
		else{
			files.add(dir);
		}
	}

}
