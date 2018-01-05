package com.activiti;

import com.activiti.mapper.JudgementMapper;
import com.activiti.pojo.user.StudentWorkInfo;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: liulinhui
 * Date: 2017/12/29
 * Time: 23:48
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChartsTest {
    @Autowired
    private JudgementMapper judgementMapper;

    @Test
    public void test() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date myDate = dateFormat.parse("2017/03/01");

        while (true) {
            if (!dateFormat.format(myDate).equalsIgnoreCase("2017/07/30")) {
                DateTime dateTime = new DateTime(myDate).plusDays(1);
                myDate = dateTime.toDate();
                Random random = new Random();
                int num = random.nextInt(20);
                if (myDate.getMonth()+1==5 && myDate.getDate()>=10 &&  myDate.getDate()<=17)
                    num=random.nextInt(10)+15;
                if ( myDate.getMonth()+1==7&& myDate.getDate()>=22&&  myDate.getDate()<=29){
                    num=random.nextInt(10)+15;
                }
                for (int i=0;i<num;i++){
                    double grade =getRandom(15,5);
                    StudentWorkInfo studentWorkInfo = new StudentWorkInfo("1500", UUID.randomUUID().toString(), grade);
                    studentWorkInfo.setLastCommitTime(myDate);
                    studentWorkInfo.setWorkDetail("Fuck的词源到了今时今日已变得众说纷纭，而且字义上是从一开始就已经是具有冒犯性，还是从某个年代开始才是，亦不能查考，但至少于15世纪的英语文献中已经可以看到这个词的踪影。虽然在英语世界具有权威地位的《牛津英语词典》（Oxford English Dictionary）也认为Fuck的词源已难考究，但主张此词是源自于盎格鲁-撒克逊");
                    judgementMapper.test(studentWorkInfo);
                }
            } else {
                break;
            }
        }
    }

    public double getRandom(double ave, double sqrt) {
        Random random = new Random();
        return Math.sqrt(sqrt) * random.nextGaussian() + ave;
    }
}
