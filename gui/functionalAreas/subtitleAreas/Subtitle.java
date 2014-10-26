package gui.functionalAreas.subtitleAreas;

import gui.VideoControlArea;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Subtitle {
	private String start;
	private String end;
	private String text;

	public Subtitle(String start, String end, String text) {
		this.start = start;
		this.end = end;
		this.text = text;
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public String getText() {
		return text;
	}

	public void setStart(String x, Subtitle sub, List<Subtitle> subsList,
			int skip) {
		if (x.matches("[0-9][0-9][:][0-9][0-9][:][0-9][0-9]")) {
			int fail = 0;
			String[] arr = x.split(":");
			int st = BasicCreateSubtitles.convert(arr[0], arr[1], arr[2]);
			String[] arr2 = sub.end.split(":");
			int sst = BasicCreateSubtitles.convert(arr2[0], arr2[1], arr2[2]);
			String[] arr3 = VideoControlArea._showDur.getText().split(":");
			int dur = BasicCreateSubtitles.convert(arr3[0], arr3[1], arr3[2]);

			if (st >= sst) {
				JOptionPane
						.showMessageDialog(
								null,
								"Start time comes after or is the same as the end time",
								"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
				fail = 1;
			} else if (st > dur || sst > dur) {
				JOptionPane.showMessageDialog(null,
						"Times outside of duration", "VAMIX Warning",
						JOptionPane.ERROR_MESSAGE);
				fail = 1;
			} else {
				for (int i = 0; i < subsList.size(); i++) {

					if (i != skip) {
						Subtitle toprobe = subsList.get(i);
						String[] arr4 = toprobe.getStart().split(":");
						int tps = BasicCreateSubtitles.convert(arr4[0],
								arr4[1], arr4[2]);
						String[] arr5 = toprobe.getEnd().split(":");
						int tpss = BasicCreateSubtitles.convert(arr5[0],
								arr5[1], arr5[2]);
						if ((st < tpss) && (sst > tps)) {
							fail = 1;
						}
					}
				}
				if (fail == 0) {
					this.start = x;
				} else {
					JOptionPane.showMessageDialog(null,
							"Edit results in clash, edit discarded",
							"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
				}

			}
		} else {
			JOptionPane.showMessageDialog(null, "Invalid input", "VAMIX Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return;

	}

	public void setEnd(String x, Subtitle sub, List<Subtitle> subsList, int skip) {
		if (x.matches("[0-9][0-9][:][0-9][0-9][:][0-9][0-9]")) {
			int fail = 0;
			String[] arr = sub.start.split(":");
			int st = BasicCreateSubtitles.convert(arr[0], arr[1], arr[2]);
			String[] arr2 = x.split(":");
			int sst = BasicCreateSubtitles.convert(arr2[0], arr2[1], arr2[2]);
			String[] arr3 = VideoControlArea._showDur.getText().split(":");
			int dur = BasicCreateSubtitles.convert(arr3[0], arr3[1], arr3[2]);

			if (st >= sst) {
				JOptionPane
						.showMessageDialog(
								null,
								"Start time comes after or is the same as the end time",
								"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
				fail = 1;
			} else if (st > dur || sst > dur) {
				JOptionPane.showMessageDialog(null,
						"Times outside of duration", "VAMIX Warning",
						JOptionPane.ERROR_MESSAGE);
				fail = 1;
			} else {
				for (int i = 0; i < subsList.size(); i++) {

					if (i != skip) {
						Subtitle toprobe = subsList.get(i);
						String[] arr4 = toprobe.getStart().split(":");
						int tps = BasicCreateSubtitles.convert(arr4[0],
								arr4[1], arr4[2]);
						String[] arr5 = toprobe.getEnd().split(":");
						int tpss = BasicCreateSubtitles.convert(arr5[0],
								arr5[1], arr5[2]);
						if ((st < tpss) && (sst > tps)) {
							fail = 1;
						}
					}
				}
				if (fail == 0) {

					this.end = x;
				} else {
					JOptionPane.showMessageDialog(null,
							"Edit results in clash, edit discarded",
							"VAMIX Warning", JOptionPane.ERROR_MESSAGE);
				}

			}
		} else {
			JOptionPane.showMessageDialog(null, "Invalid input", "VAMIX Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return;

	}

	public void setText(String x) {
		if (!(x.length() > 40)) {
			this.text = x;
		} else {
			JOptionPane.showMessageDialog(null,
					"Edit too long, edit discarded", "VAMIX Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return;
	}

	public String toString() {
		return start + " " + end + " " + text;
	}
}
