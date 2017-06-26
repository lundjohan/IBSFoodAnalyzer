package stat_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.model.TagTemplate;
import com.ibsanalyzer.calc_score_classes.CalcScore;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.ArrayList;
import java.util.List;


public class TagPointHandler {

	/**
	 * Can I make it so calcScore only does the calculation of the score?
	 * @param chunks
	 * @param calcScore
	 * @return
	 * @throws Exception
	 */
	public static List<TagPoint> retrieveTagPoints(List<Chunk>chunks, List<TagTemplate>allTagTemplates, CalcScore calcScore) throws Exception {
		List<TagPoint>tagPoints = new ArrayList<>();


		for (TagTemplate tt: allTagTemplates) {
			//too inefficient??? Alternatives? Positive => pretty easy to continue with it. if days hasnt been added. But they most surely will have been.
			//better solution => add all eftervart. Mindre sällan sker att man ändrar om i tidiga dagar.
			TagPoint tp = calcScore.doCalc(chunks, tt);
			if (tp != null){
				tagPoints.add(tp);
			}
		}
		return tagPoints;
	}
}
