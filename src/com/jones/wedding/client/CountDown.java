package com.jones.wedding.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.jones.wedding.client.util.Metrics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 *
 */
public class CountDown implements EntryPoint, Metrics
{
	private Label myClock;

	private long myDifference;

	public static final String kWeddingDate = "05-19-2012";

	public static final String kDateTimeFormatString = "MM-dd-yyyy";

	public static final String kDateTimeFormatString2 = "MM-dd-yyyy hh:mm:ss";

	public static final DateTimeFormat kDateTimeFormat = DateTimeFormat.getFormat(kDateTimeFormatString2);

	public static final int kUpdateInterval = 1000;

	private HorizontalPanel mySecondHolder;

	public void onModuleLoad()
	{
		Date myWeddingDate = DateTimeFormat.getFormat(kDateTimeFormatString).parse(kWeddingDate);
		myDifference = myWeddingDate.getTime() - System.currentTimeMillis();
		myClock = new Label();
		VerticalPanel aHolder = new VerticalPanel();
		aHolder.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		aHolder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		aHolder.setWidth("100%");
		aHolder.setHeight("100%");
		//aHolder.add(myClock);
		mySecondHolder = new HorizontalPanel();
		mySecondHolder.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		aHolder.add(mySecondHolder);
		aHolder.addStyleName("clock");
		SimplePanel aTransMiddlePanel = new SimplePanel();
		SimplePanel aTransMiddle = new SimplePanel();
		aTransMiddlePanel.add(aTransMiddle);
		aTransMiddle.add(new Label());
		aTransMiddle.addStyleName("transbackround");
		aTransMiddlePanel.addStyleName("transbackround-container");
		RootPanel.get().add(aTransMiddlePanel);
		RootPanel.get().add(aHolder);
		updateDate();
		new Timer()
		{
			@Override
			public void run()
			{
				updateDate();
			}
		}.scheduleRepeating(kUpdateInterval);
	}

	/**
	 * 
	 */
	private void updateDate()
	{
		myDifference = myDifference - kSecond;
		long aDays = myDifference / kDay;
		long aLeftOver = myDifference % kDay;
		long aHours = aLeftOver / kHour;
		long aLeftOver2 = aLeftOver % kHour;
		long aMinutes = aLeftOver2 / kMinute;
		long aSeconds = ((aLeftOver2) % kMinute) / kSecond;
		myClock.setText(aDays + " Days " + aHours + " Hours " + aMinutes + " Minutes " + aSeconds + " Seconds");

		mySecondHolder.clear();

		String aString = aDays + "";
		mySecondHolder.add(getImage((aString.length() != 3 ? "0" : aString.substring(0, 1)) + ".png"));
		mySecondHolder.add(getImage((aString.length() == 1 ? "0" : (aString.length() == 2 ? aString.substring(0, 1) : aString.substring(1, 2))) + ".png"));
		mySecondHolder.add(getImage(aString.substring(aString.length() - 1) + ".png"));

		aString = aHours + "";
		mySecondHolder.add(getImage((aString.length() == 1 ? "0" : aString.substring(0, 1)) + ".png"));
		mySecondHolder.add(getImage(aString.substring(aString.length() == 1 ? 0 : 1) + ".png"));

		mySecondHolder.add(getImage("Colon.png"));
		aString = aMinutes + "";
		mySecondHolder.add(getImage((aString.length() == 1 ? "0" : aString.substring(0, 1)) + ".png"));
		mySecondHolder.add(getImage(aString.substring(aString.length() == 1 ? 0 : 1) + ".png"));

		mySecondHolder.add(getImage("Colon.png"));
		aString = aSeconds + "";
		mySecondHolder.add(getImage((aString.length() == 1 ? "0" : aString.substring(0, 1)) + ".png"));
		mySecondHolder.add(getImage(aString.substring(aString.length() == 1 ? 0 : 1) + ".png"));
	}

	private Image getImage(String theUrl)
	{
		Image anImage = new Image(theUrl);
		anImage.addStyleName("counting-image");
		return anImage;
	}
}
