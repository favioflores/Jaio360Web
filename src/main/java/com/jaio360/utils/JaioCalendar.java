package com.jaio360.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Favio
 */
public class JaioCalendar extends GregorianCalendar{
      /**

                *

                 */

                private static final long serialVersionUID = 1L;

 

                private Date dtTime;

 

                private int inDate;

 

                private int inDayOfMonth;

 

                private int inDayOfWeek;

 

                private int inDayOfYear;

 

                private int inHourOfDay;

 

                private int inMilliSecond;

 

                private int inMinute;

 

                private int inMonth;

 

                private int inSecond;

 

                private int inYear;

 

                /**

                * Constructor

                */

                public JaioCalendar() {

                               toSetJaioCalendar();

                }

 

                /**

                * Constructor with parameter to set the time

                *

                 * @param objDt

                *            <code>Date</code>

                */

                public JaioCalendar(Date objDt) {

                               setDtTime(objDt);

                               toSetJaioCalendar();

                }

 

                /**

                * Returns the dtTime

                *

                 * @return <code>String</code>

                */

 

                public Date getDtTime() {

                               if (dtTime == null) {

                                               this.dtTime = super.getTime();

                               }

                               return dtTime;

                }

 

                /**

                * Returns the inDate

                *

                 * @return <code>int</code>

                */

 

                public int getInDate() {

                               return inDate;

                }

 

                /**

                * Returns the inDayOfMonth

                *

                 * @return <code>int</code>

                */

 

                public int getInDayOfMonth() {

                               return inDayOfMonth;

                }

 

                /**

                * Returns the inDayOfWeek

                *

                 * @return <code>int</code>

                */

 

                public int getInDayOfWeek() {

                               return inDayOfWeek;

                }

 

                /**

                * Returns the inDayOfYear

                *

                 * @return <code>int</code>

                */

 

                public int getInDayOfYear() {

                               return inDayOfYear;

                }

 

                /**

                * Returns the inHourOfDay

                *

                 * @return <code>int</code>

                */

 

                public int getInHourOfDay() {

                               return inHourOfDay;

                }

 

                /**

                * Returns the inMilliSecond

                *

                 * @return <code>int</code>

                */

 

                public int getInMilliSecond() {

                               return inMilliSecond;

                }

 

                /**

                * Returns the inMinute

                *

                 * @return <code>int</code>

                */

 

                public int getInMinute() {

                               return inMinute;

                }

 

                /**

                * Returns the inMonth

                *

                 * @return <code>int</code>

                */

 

                public int getInMonth() {

                               return inMonth;

                }

 

                /**

                * Returns the inSecond

                *

                 * @return <code>int</code>

                */

 

                public int getInSecond() {

                               return inSecond;

                }

 

                /**

                * Returns the inYear

                *

                 * @return <code>int</code>

                */

 

                public int getInYear() {

                               return inYear;

                }

 

                /**

                * Sets the value for strTime

                * @param objDt

                */

                public void setDtTime(Date objDt) {

                               super.setTime(objDt);

                               this.dtTime = super.getTime();

                }

 

                /**

                * Sets the value for inDate

                *

                 * @param inDate

                *            <code>int</code>

                */

 

                public void setInDate(int inDate) {

                               this.inDate = inDate;

                }

 

                /**

                * Sets the value for inDayOfMonth

                *

                 * @param inDayOfMonth

                *            <code>int</code>

                */

 

                public void setInDayOfMonth(int inDayOfMonth) {

                               this.inDayOfMonth = inDayOfMonth;

                }

 

                /**

                * Sets the value for inDayOfWeek

                *

                 * @param inDayOfWeek

                *            <code>int</code>

                */

 

                public void setInDayOfWeek(int inDayOfWeek) {

                               this.inDayOfWeek = inDayOfWeek;

                }

 

                /**

                * Sets the value for inDayOfYear

                *

                 * @param inDayOfYear

                *            <code>int</code>

                */

 

                public void setInDayOfYear(int inDayOfYear) {

                               this.inDayOfYear = inDayOfYear;

                }

 

                /**

                * Sets the value for inHourOfDay

                *

                 * @param inHourOfDay

                *            <code>int</code>

                */

 

                public void setInHourOfDay(int inHourOfDay) {

                               this.inHourOfDay = inHourOfDay;

                }

 

                /**

                * Sets the value for inMilliSecond

                *

                 * @param inMilliSecond

                *            <code>int</code>

                */

 

                public void setInMilliSecond(int inMilliSecond) {

                               this.inMilliSecond = inMilliSecond;

                }

 

                /**

                * Sets the value for inMinute

                *

                 * @param inMinute

                *            <code>int</code>

                */

 

                public void setInMinute(int inMinute) {

                               this.inMinute = inMinute;

                }

 

                /**

                * Sets the value for inMonth

                *

                 * @param inMonth

                *            <code>int</code>

                */

 

                public void setInMonth(int inMonth) {

                               this.inMonth = inMonth;

                }

 

                /**

                * Sets the value for inSecond

                *

                 * @param inSecond

                *            <code>int</code>

                */

 

                public void setInSecond(int inSecond) {

                               this.inSecond = inSecond;

                }

 

                /**

                * Sets the value for inYear

                *

                 * @param inYear

                *            <code>int</code>

                */

 

                public void setInYear(int inYear) {

                               this.inYear = inYear;

                }

 

                /**

                * Sets the default values for properties

                */

                public void toSetJaioCalendar() {

                               this.inDate = super.get(Calendar.DATE);

                               this.inDayOfWeek = super.get(Calendar.DAY_OF_WEEK);

                               this.inDayOfMonth = super.get(Calendar.DAY_OF_MONTH);

                               this.inDayOfYear = super.get(Calendar.DAY_OF_YEAR);

                               this.inMonth = super.get(Calendar.MONTH);

                               this.inYear = super.get(Calendar.YEAR);

                               this.inHourOfDay = super.get(Calendar.HOUR_OF_DAY);

                               this.inMinute = super.get(Calendar.MINUTE);

                               this.inSecond = super.get(Calendar.SECOND);

                               this.inMilliSecond = super.get(Calendar.MILLISECOND);

                }
}
