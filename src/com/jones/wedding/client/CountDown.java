package com.jones.wedding.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.jones.wedding.client.util.Metrics;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CountDown implements EntryPoint, Metrics
{
	private long myDifference;

	public static final String kWeddingDate = "05-19-2012";

	public static final String kDateTimeFormatString = "MM-dd-yyyy";

	public static final String kDateTimeFormatString2 = "MM-dd-yyyy hh:mm:ss";

	public static final DateTimeFormat kDateTimeFormat = DateTimeFormat.getFormat(kDateTimeFormatString2);

	public static final int kUpdateInterval = 1000;

	private FlowPanel myClockHolder;

	private Map<Integer, FallingImage> myImageMap = new HashMap<Integer, FallingImage>();

	public void onModuleLoad()
	{
		Date myWeddingDate = DateTimeFormat.getFormat(kDateTimeFormatString).parse(kWeddingDate);
		myDifference = myWeddingDate.getTime() - System.currentTimeMillis();
		VerticalPanel aHolder = new VerticalPanel();
		aHolder.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		aHolder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		myClockHolder = new FlowPanel();
		myClockHolder.addStyleName("clockHolder");
		aHolder.add(myClockHolder);
		aHolder.addStyleName("clock");
		FlowPanel aTransMiddlePanel = new FlowPanel();
		SimplePanel aTransMiddle = new SimplePanel();
		aTransMiddlePanel.add(aTransMiddle);
		aTransMiddle.add(new Label());
		aTransMiddle.addStyleName("transbackround");
		aTransMiddlePanel.addStyleName("transbackround-container");
		aTransMiddle = new SimplePanel();
		aTransMiddle.add(new Label());
		aTransMiddle.addStyleName("bottom-background");
		aTransMiddlePanel.add(aTransMiddle);
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

		String aString = aDays + "";
		getImage((aString.length() != 3 ? "0" : aString.substring(0, 1)) + ".png", 0);
		getImage((aString.length() == 1 ? "0" : (aString.length() == 2 ? aString.substring(0, 1) : aString.substring(1, 2))) + ".png", 1);
		getImage(aString.substring(aString.length() - 1) + ".png", 2);

		getImage("oneBlankPixel.png", 3);
		aString = aHours + "";
		getImage((aString.length() == 1 ? "0" : aString.substring(0, 1)) + ".png", 4);
		getImage(aString.substring(aString.length() == 1 ? 0 : 1) + ".png", 5);

		getImage("Colon.png", 6);
		aString = aMinutes + "";
		getImage((aString.length() == 1 ? "0" : aString.substring(0, 1)) + ".png", 7);
		getImage(aString.substring(aString.length() == 1 ? 0 : 1) + ".png", 8);

		getImage("Colon.png", 9);
		aString = aSeconds + "";
		getImage((aString.length() == 1 ? "0" : aString.substring(0, 1)) + ".png", 10);
		getImage(aString.substring(aString.length() == 1 ? 0 : 1) + ".png", 11);
	}

	private void getImage(String theUrl, int thePosition)
	{
		FallingImage anImage = new FallingImage(theUrl, thePosition);
		FallingImage anOldImage = myImageMap.get(thePosition);
		if(anOldImage != null && anOldImage.getUrl().endsWith(theUrl))
		{
			return;
		}
		myImageMap.put(thePosition, anImage);
		myClockHolder.insert(anImage, 0);
		if(anOldImage != null)
		{
			anOldImage.startFall();
		}
	}

	private class FallingImage extends Image
	{
		private static final int kRemoveDelay = 2000;

		public FallingImage(String theUrl, int thePosition)
		{
			super(theUrl);
			addStyleName("counting-image");
			addStyleName("counting-image-" + thePosition);
		}

		/**
		 * set the css style for falling, sched
		 */
		public void startFall()
		{
			this.addStyleName("falling");
			new Timer()
			{
				public void run()
				{
					FallingImage.this.removeFromParent();
				}
			}.schedule(kRemoveDelay);
		}
	}
}
