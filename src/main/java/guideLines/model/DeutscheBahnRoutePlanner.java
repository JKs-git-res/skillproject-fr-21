package main.java.guideLines.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DeutscheBahnRoutePlanner {
	
	public void getRouteInfo(Station departure, Station destination) throws IOException {
		String start = URLEncoder.encode(departure.getName() + " " + departure.getCity(), "UTF-8");
		String stop = URLEncoder.encode(destination.getName() + " " + departure.getCity(), "UTF-8");
		Document doc = Jsoup.connect("https://reiseauskunft.bahn.de/bin/query.exe/dn?"
				+ "revia=yes"
				+ "&existOptimizePrice=1"
				+ "&country=DEU"
				+ "&dbkanal_007=L01_S01_D001_KIN0001_qf-bahn-svb-kl2_lz03"
				+ "&start=1"
				+ "&protocol=https%3A&REQ0JourneyStopsS0A+"
				+ "&REQ0JourneyStopsSID="
				+ "&S=" + start
				+ "&Z=" + stop
				+ "&REQ0JourneyStopsZID="
				+ "&date=" + getDateFormated()
				+ "&time=" + getTimeFormatted()
				+ "&timesel=depart"
				+ "&returnDate="
				+ "&returnTime="
				+ "&returnTimesel=depart"
				+ "&optimize=0"
				+ "&auskunft_travelers_number=1"
				+ "&tariffTravellerType.1=E"
				+ "&tariffTravellerReductionClass.1=0"
				+ "&tariffClass=2"
				+ "&rtMode=DB-HYBRID"
				+ "&externRequest=yes"
				+ "&HWAI=JS%21js%3Dyes%21ajax%3Dyes%21").get();
		Elements table = doc.getElementsByClass("station");
		System.out.println(table);
	}
	
	private String getDateFormated() throws UnsupportedEncodingException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd.MM.yy", new Locale("de", "DE"));
		return URLEncoder.encode(simpleDateFormat.format(new Date()).toString(), "UTF-8");
	}
	
	private String getTimeFormatted() throws UnsupportedEncodingException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		return URLEncoder.encode(simpleDateFormat.format(new Date()).toString(), "UTF-8");
	}

}
