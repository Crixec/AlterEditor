package crixec.commom.widget;

import android.graphics.*;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.text.TextUtils;
import android.content.res.TypedArray;
import crixec.commom.main.R;

public class AlterEditor extends EditText
{
	private Rect rect;
	private Paint paint;
	private Paint deviderLine;
	private int lastLineLevel;
	private int lastLineCount;
	private int basePadding = 8;
	private int lineNumberPadding = basePadding;
	private int codeColor;
	private int deviderColor;
	private int lineNumberColor;
	public AlterEditor(Context context)
	{
		super(context);
		init(context);
	}
	public AlterEditor(Context context, AttributeSet attrs)
	{
        super(context, attrs);
		TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.AlterEditorAttridutes);
		codeColor = typed.getColor(R.styleable.AlterEditorAttridutes_codeColor, Color.BLACK);
		deviderColor = typed.getColor(R.styleable.AlterEditorAttridutes_deviderColor, Color.GRAY);
		lineNumberColor = typed.getColor(R.styleable.AlterEditorAttridutes_lineNumberColor, Color.parseColor("#212121"));
		basePadding = (int) typed.getDimension(R.styleable.AlterEditorAttridutes_basePadding, 8);
		typed.recycle();
		init(context);
    }

	private void init(Context context)
	{
		if (rect == null)
		{
			setInputType(InputType.TYPE_CLASS_TEXT |
                         InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                         InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
			setGravity(Gravity.START|Gravity.TOP);
			setEllipsize(TextUtils.TruncateAt.END);
			setTypeface(Typeface.MONOSPACE);
			setTextSize(14);
			setPadding(basePadding, 0, 0, 0);
			rect = new Rect();
			paint = new Paint();
			paint.setFakeBoldText(true);
			paint.setAntiAlias(true);
			deviderLine = new Paint();
			deviderLine.setColor(deviderColor);
			paint.setStyle(Paint.Style.FILL);
			paint.setTypeface(Typeface.MONOSPACE);
			paint.setTextSize(getTextSize() * 8 / 10);
			paint.setColor(lineNumberColor);
			setTextColor(codeColor);
		}
	}

	@Override
	protected void onSelectionChanged(int selStart, int selEnd)
	{
		// TODO: Implement this method
		super.onSelectionChanged(selStart, selEnd);
	}

	private String appendWhiteSpaces(int lineNumber, int lineCount)
	{
		StringBuilder lineText = new StringBuilder();
		int fakeLN = lineNumber + 1;
		int wsCount =  String.valueOf(lineCount).length() - String.valueOf(fakeLN).length();
		for (int i = 0; i < wsCount; i++)
		{
			lineText.append(" ");
		}
		lineText.append(fakeLN);
		return lineText.toString();

	}
	private void adjustPadding(String maxLineText){
		int textWdth = (int) paint.measureText(maxLineText);
		lineNumberPadding = basePadding + textWdth;
		setPadding(lineNumberPadding, 0, 0, 0);
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		int lineHeight = getBaseline();
		String maxLineText = appendWhiteSpaces(getLineCount() - 1, getLineCount());
		int width = lineNumberPadding - basePadding + 1;
		canvas.drawLine(width, 0, width, getHeight(), deviderLine);
		if(lastLineCount != getLineCount()){
			adjustPadding(maxLineText);
			lastLineCount = getLineCount();
		}
		for (int i = 0; i < getLineCount(); i++)
		{
			int lineLevel = String.valueOf(getLineCount() - 1).length();
			StringBuilder lineText = new StringBuilder(appendWhiteSpaces(i, getLineCount()));
			if (lineLevel > lastLineLevel)
			{
				lastLineLevel = lineLevel;
				adjustPadding(maxLineText);
			}
			canvas.drawText(lineText.toString(), rect.left, lineHeight, paint);
			lineHeight += getLineHeight();
		}
	}

}
