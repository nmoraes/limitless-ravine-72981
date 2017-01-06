package db;

public final class Util {

	private Util() {
		super();
	}

	public static String createHTML(String reportID, String html) {

		String finalHtml = html.replace("reportIdDb", reportID);
				
		return finalHtml;

	}

}
