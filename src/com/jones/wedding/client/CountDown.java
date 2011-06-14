package com.jones.wedding.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.jones.wedding.client.images.ClockImageBundle;
import com.jones.wedding.client.util.Metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * This GWT module displays a clock of days, hours, minutes, seconds until 5-19-2012.  Makes good use
 * of CSS3 transitions to animate in modern browsers
 */
public class CountDown implements EntryPoint, Metrics
{
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

	private long myWeddingTime;

	private ClockImageBundle myBundle;

	/**
	 * Setup global stuff, attach panels to the DOM and start the countdown timers.
	 */
	public void onModuleLoad()
	{
		myBundle = GWT.<ClockImageBundle>create(ClockImageBundle.class);
		myWeddingTime = DateTimeFormat.getFormat(kDateTimeFormatString).parse(kWeddingDate).getTime();
		SimplePanel aHolder = new SimplePanel();
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
		aTransMiddle.addStyleName("transbackground");
		aTransMiddlePanel.addStyleName("transbackground-container");
		aTransMiddle = new SimplePanel();
		aTransMiddle.add(new Label());
		aTransMiddle.addStyleName("bottom-background");
		aTransMiddlePanel.add(aTransMiddle);
		Label aLabel = new Label("Matthew & Maureen are getting married in");
		aLabel.addStyleName("topText");
		RootPanel.get().add(aTransMiddlePanel);
		RootPanel.get().add(aHolder);
		SimplePanel aLabelHolder = new SimplePanel();
		aLabelHolder.addStyleName("clock");
		aLabelHolder.add(aLabel);
		RootPanel.get().add(aLabelHolder);
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
		long aDifference = myWeddingTime - System.currentTimeMillis();
		long aDays = aDifference / kDay;
		long aLeftOver = aDifference % kDay;
		long aHours = aLeftOver / kHour;
		long aLeftOver2 = aLeftOver % kHour;
		long aMinutes = aLeftOver2 / kMinute;
		long aSeconds = ((aLeftOver2) % kMinute) / kSecond;

		String aString = aDays + "";
		updateImageColumn(getImageResource(aString.length() != 3 ? "0" : aString.substring(0, 1)), 0);
		updateImageColumn(getImageResource(aString.length() == 1 ? "0" : (aString.length() == 2 ? aString.substring(0, 1) : aString.substring(1, 2))), 1);
		updateImageColumn(getImageResource(aString.substring(aString.length() - 1)), 2);

		updateImageColumn(myBundle.oneBlankPixel(), 3);
		aString = aHours + "";
		updateImageColumn(getImageResource(aString.length() == 1 ? "0" : aString.substring(0, 1)), 4);
		updateImageColumn(getImageResource(aString.substring(aString.length() == 1 ? 0 : 1)), 5);

		updateImageColumn(myBundle.colon(), 6);
		aString = aMinutes + "";
		updateImageColumn(getImageResource(aString.length() == 1 ? "0" : aString.substring(0, 1)), 7);
		updateImageColumn(getImageResource(aString.substring(aString.length() == 1 ? 0 : 1)), 8);

		updateImageColumn(myBundle.colon(), 9);
		aString = aSeconds + "";
		updateImageColumn(getImageResource(aString.length() == 1 ? "0" : aString.substring(0, 1)), 10);
		updateImageColumn(getImageResource(aString.substring(aString.length() == 1 ? 0 : 1)), 11);
	}

	/**
	 * Translate a string to the proper image resource from our bundle
	 *
	 * @param theString the string
	 * @return imageResource from our bundle, or null if it doesn't match 0-9
	 */
	private ImageResource getImageResource(String theString)
	{
		if(theString.equals("0"))
		{
			return myBundle.image0();
		} else if(theString.equals("1"))
		{
			return myBundle.image1();
		} else if(theString.equals("2"))
		{
			return myBundle.image2();
		} else if(theString.equals("3"))
		{
			return myBundle.image3();
		} else if(theString.equals("4"))
		{
			return myBundle.image4();
		} else if(theString.equals("5"))
		{
			return myBundle.image5();
		} else if(theString.equals("6"))
		{
			return myBundle.image6();
		} else if(theString.equals("7"))
		{
			return myBundle.image7();
		} else if(theString.equals("8"))
		{
			return myBundle.image8();
		} else if(theString.equals("9"))
		{
			return myBundle.image9();
		}
		return null;
	}

	/**
	 * If there is an old image for the index, and the URL is the same, do nothing.
	 * If the url doesn't match or there isn't an older image, generate a new one and put it
	 * into the DOM.  If there was an older image, trigger that image's falling state
	 *
	 * @param theResource the image url to use
	 * @param thePosition the position on the page
	 */
	private void updateImageColumn(ImageResource theResource, int thePosition)
	{
		FallingImage anOldImage = myImageMap.get(thePosition);
		if(anOldImage != null && anOldImage.getUrl().equals(theResource.getURL()))
		{
			return;
		}
		FallingImage anImage = new FallingImage(theResource, thePosition);
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
		private static final int kRemoveDelay = 1250;

		public FallingImage(ImageResource theResource, int thePosition)
		{
			super(theResource);
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
					Scheduler.get().scheduleDeferred(new Command()
					{
						public void execute()
						{
							FallingImage.this.removeFromParent();
						}
					});
				}
			}.schedule(kRemoveDelay);
		}
	}
}
