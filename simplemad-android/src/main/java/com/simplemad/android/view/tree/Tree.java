package com.simplemad.android.view.tree;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.setting.DefaultDivider;
import com.simplemad.android.view.setting.Divider;

public final class Tree {
	
	private Context _context;
	
	private FrameLayout _scrollView;
	
	private LinearLayout _layout;
	
	private Node _root;
	
	private LeafClickListener _listener;
	
	private TreeSetting _setting;
	
	private Divider _divider;
	
	private boolean _isHorizontal;

	private DasOnGroupExpandListener _expandListener;

	private DasOnGroupCollapseListener _collapseListener;
	
	private boolean _isCollapseOthers;

	public Tree(Context context, boolean isHorizontal, TreeSetting setting, boolean isCollapseOthers) {
		this._context = context;
		this._isHorizontal = isHorizontal;
		this._isCollapseOthers = isCollapseOthers;
		this._setting = setting;
		if(this._setting == null)
			this._divider = new DefaultDivider();
		else 
			this._divider = this._setting.divider();
		initUI();
	}
	
	private void initUI() {
		_layout = new LinearLayout(_context);
		
		if(_setting != null)
			StyleSettingTool.setPadding(_layout, _setting.padding());
		_root = new Node(_context, null, _isCollapseOthers);
		if(_isHorizontal) {
			_layout.setOrientation(LinearLayout.HORIZONTAL);
			_scrollView = new HorizontalScrollView(_context);
			_scrollView.setHorizontalScrollBarEnabled(false);
		}
		else {
			_layout.setOrientation(LinearLayout.VERTICAL);
			_scrollView = new ScrollView(_context);
			_scrollView.setVerticalScrollBarEnabled(false);
		}
		StyleSettingTool.setBackground(_scrollView, _setting);
		
		_scrollView.addView(_layout, frameParams());
	}
	
	public boolean isHorizontal() {
		return _isHorizontal;
	}
	
	public void setLayoutAnimation(LayoutAnimationController controller) {
		_layout.setLayoutAnimation(controller);
	}
	
	private FrameLayout.LayoutParams frameParams() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
		return params;
	}
	
	public void setLeafClickListener(LeafClickListener listener) {
		_listener = listener;
	}
	
	public void setDasOnGroupExpandListener(DasOnGroupExpandListener listener) {
		this._expandListener = listener;
	}
	
	public void setDasOnGroupCollapseListener(DasOnGroupCollapseListener listener) {
		this._collapseListener = listener;
	}
	
	/**
	 * 
	 * @param parent
	 * @param target
	 * @return the new node added
	 */
	public Node addChild(Node parent, View target) {
		if(parent == null)
			return addChildAtIndex(parent, target, _root.children().size());
		else
			return addChildAtIndex(parent, target, parent.children().size());
	}
	
	public Node addChildAtIndex(Node parent, View target, int index) {
		Node child = new Node(_context, target, _isCollapseOthers);
		if(parent == null)
			_root.addChildAtIndex(child, index);
		else
			parent.addChildAtIndex(child, index);
		return child;
	}
	
	public List<Node> addChildren(Node parent, List<View> views) {
		List<Node> nodes = new ArrayList<Node>();
		for(View target : views) {
			nodes.add(addChild(parent, target));
		}
		return nodes;
	}
	
	public Node getRoot() {
		return _root;
	}
	
	public View view() {
		refresh();
		return _scrollView;
	}
	
	public void refresh() {
		draw(_root);
		attachEvent(_root);
		_root.click();
	}
	
	/*references to Node method below*/
	/*start Node method*/
	public boolean isRoot(Node node) {
		return node.isRoot();
	}
	
	public boolean hasChildren(Node node) {
		return node.hasChildren();
	}
	
	public List<Node> children(Node node) {
		return node.children();
	}
	
	public Node parent(Node node) {
		return node.parent();
	}
	
	public View target(Node node) {
		return node.target();
	}
	
	public int level(Node node) {
		return node.level();
	}
	
	public void select(Node node) {
		if(node == null)
			return;
		if(node.isDisplay()) {
			node.click();
		} else {
			Node child = node;
			Node parent = child.parent();
			while(parent != null) {
				if(child.isDisplay()) {
					child.click();
					parent = null;
				} else {
					parent.openChildren();
					child.click();
					child = parent;
					parent = child.parent();
				}
			}
		}
	}
	/*end Node method*/
	
	private void attachEvent(final Node root) {
		if(root == null)
			return;
		for(final Node child : root.children()) {
			if(!child.hasChildren() && _listener != null)
				child.setLeafClickListener(_listener);
			else if(child.hasChildren()) {
				attachEvent(child);
			}
		}
		if(root.hasChildren()) {
			root.setDasOnGroupCollapseListener(_collapseListener);
			root.setDasOnGroupExpandListener(_expandListener);
		} else {
			root.setLeafClickListener(_listener);
		}
	}
	
	private void draw(Node root) {
		boolean isRoot = _root.equals(root);
		if(isRoot) {
			addDivider(new View(_context));
		} else {
			_layout.addView(root.target(), childParams(root.level()));
			addDivider(root.divider());
		}
		for(int index = 0; index < root.children().size(); index++) {
			Node childNode = root.children().get(index);
			draw(childNode);				
		}
	}
	
	private LinearLayout.LayoutParams childParams(int level) {
		LinearLayout.LayoutParams params = null;
		if(_isHorizontal) {
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		} else {
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		params.weight = 1;
		if(_setting != null) {
			if(_isHorizontal)
				params.topMargin = _setting.leftMargin() *  level;
			else 
				params.leftMargin = _setting.leftMargin() * level;
		}
		return params;
	}
	
	private void addDivider(View dividerView) {
		if(_divider == null)
			return;
		StyleSettingTool.setBackground(dividerView, _divider);
		_layout.addView(dividerView, dividerParams(_divider));
	}
	
	private LinearLayout.LayoutParams dividerParams(Divider divider) {
		LinearLayout.LayoutParams params = null;
		if(_isHorizontal) {
			params = new LinearLayout.LayoutParams(divider.getHeight(), divider.getWidth());
		} else {
			params = new LinearLayout.LayoutParams(divider.getWidth(), divider.getHeight());
		}
		return params;
	}
	
}
