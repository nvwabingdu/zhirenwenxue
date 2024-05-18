package cn.dreamfruits.yaoguo.view.mention.util;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.dreamfruits.yaoguo.view.mention.bean.FormatItemResult;
import cn.dreamfruits.yaoguo.view.mention.bean.FormatRange;
import cn.dreamfruits.yaoguo.view.mention.bean.FormatResult;
import cn.dreamfruits.yaoguo.view.mention.bean.MentionTopic;
import cn.dreamfruits.yaoguo.view.mention.bean.MentionUser;
import cn.dreamfruits.yaoguo.view.mention.bean.Range;

public class FormatRangeManager extends RangeManager {

    public FormatResult getFormatResult(String text) {
        FormatResult result = new FormatResult();
        result.setText(text);
        if (isEmpty()) {
            return result;
        }

        ArrayList<? extends Range> ranges = get();
        Collections.sort(ranges);

        List<FormatItemResult> userResultList = new ArrayList<>();
        List<FormatItemResult> topicResultList = new ArrayList<>();
        for (Range range : ranges) {
            if (range instanceof FormatRange) {
                FormatRange formatRange = (FormatRange) range;
                FormatRange.FormatData convert = formatRange.getConvert();
                if (formatRange.getInsertData() instanceof MentionUser) {
                    FormatItemResult userResult = (FormatItemResult) convert.formatResult();
                    userResult.setFromIndex(range.getFrom());
                    userResult.setLength(range.getTo() - range.getFrom());
                    userResultList.add(userResult);
                } else if (formatRange.getInsertData() instanceof MentionTopic) {
                    FormatItemResult topicResult = (FormatItemResult) convert.formatResult();
                    topicResult.setFromIndex(range.getFrom());
                    topicResult.setLength(range.getTo() - range.getFrom());
                    topicResultList.add(topicResult);
                }
            }
        }
        result.setUserList(userResultList);
        result.setTopicList(topicResultList);
        return result;
    }
}
