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
 * This GWT module displays a clock of days, hours, minutes, seconds until 5-12-2012.  Makes good use
 * of CSS3 transitions to animate in modern browsers
 */
public class CountDown implements EntryPoint, Metrics
{
	/**
	 * The last time we used
	 */
	private long myDifference;

	/**
	 * Constant date to count to
	 */
	public static final String kWeddingDate = "05-19-2012";

	/**
	 * format of the date
	 */
	public static final String kDateTimeFormatString = "MM-dd-yyyy";

	/**
	 * Holder on the page of the "clock"
	 */
	private FlowPanel myClockHolder;

	/**
	 * Map of columns to integers on the page, used so we can get the last used for the column
	 * to check if an update to the URL has occurred.
	 */
	private Map<Integer, FallingImage> myImageMap = new HashMap<Integer, FallingImage>();

	/**
	 * Setup global stuff, attach panels to the DOM and start the countdown timers.
	 */
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
		Label aDays = new Label("Days");
		aDays.addStyleName("days");
		myClockHolder.add(aDays);
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
		Label aLabel = new Label("Matthew & Maureen are getting married in");
		aLabel.addStyleName("topText");
		aHolder.add(aLabel);
		RootPanel.get().add(aTransMiddlePanel);
		RootPanel.get().add(aHolder);
		updateDate();
		//Start the timer to trigger an update
		new Timer()
		{
			@Override
			public void run()
			{
				updateDate();
			}
		}.scheduleRepeating(kSecond);
	}

	/**
	 * Calculate the new times and trigger updates for columns potentially
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
		updateImageColumn((aString.length() != 3 ? "0" : aString.substring(0, 1)) + ".png", 0);
		updateImageColumn((aString.length() == 1 ? "0" : (aString.length() == 2 ? aString.substring(0, 1) : aString.substring(1, 2))) + ".png", 1);
		updateImageColumn(aString.substring(aString.length() - 1) + ".png", 2);

		updateImageColumn("oneBlankPixel.png", 3);
		aString = aHours + "";
		updateImageColumn((aString.length() == 1 ? "0" : aString.substring(0, 1)) + ".png", 4);
		updateImageColumn(aString.substring(aString.length() == 1 ? 0 : 1) + ".png", 5);

		updateImageColumn("Colon.png", 6);
		aString = aMinutes + "";
		updateImageColumn((aString.length() == 1 ? "0" : aString.substring(0, 1)) + ".png", 7);
		updateImageColumn(aString.substring(aString.length() == 1 ? 0 : 1) + ".png", 8);

		updateImageColumn("Colon.png", 9);
		aString = aSeconds + "";
		updateImageColumn((aString.length() == 1 ? "0" : aString.substring(0, 1)) + ".png", 10);
		updateImageColumn(aString.substring(aString.length() == 1 ? 0 : 1) + ".png", 11);
	}

	/**
	 * If there is an old image for the index, and the URL is the same, do nothing.
	 * If the url doesn't match or there isn't an older image, generate a new one and put it
	 * into the DOM.  If there was an older image, trigger that image's falling state
	 *
	 * @param theUrl the image url to use
	 * @param thePosition the position on the page
	 */
	private void updateImageColumn(String theUrl, int thePosition)
	{
		FallingImage anOldImage = myImageMap.get(thePosition);
		if(anOldImage != null && anOldImage.getUrl().endsWith(theUrl))
		{
			return;
		}
		FallingImage anImage = new FallingImage(theUrl, thePosition);
		myImageMap.put(thePosition, anImage);
		myClockHolder.insert(anImage, 0);
		if(anOldImage != null)
		{
			anOldImage.startFall();
		}
	}

	/**
	 * Image with a method that triggers a falling state.  Also removes from the DOM after a set
	 * amount of time.  Finally adds some base css classes.
	 */
	private class FallingImage extends Image
	{
		/**
		 * Delay before removing from the DOM
		 */
		private static final int kRemoveDelay = 2000;

		public FallingImage(String theUrl, int thePosition)
		{
			super(theUrl);
			addStyleName("counting-image");
			addStyleName("counting-image-" + thePosition);
		}

		/**
		 * set the css style for falling, schedule timer to remove from the DOM
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
