package com.simplemad.android.view.tree;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.simplemad.android.util.CollectionUtil;

/**
 * tree node {@link Tree}
 * @author admin
 *
 */
public final class Node {
	
	private Node _parent;
	
	private List<Node> _children = new ArrayList<Node>();
	
	private View _divider;
	
	private View _target;
	
	private LeafClickListener _listener;
	
	private DasOnGroupCollapseListener _collapseListener;
	
	private DasOnGroupExpandListener _expandListener;
	
	private Context _context;
	
	private int _level;
	
	private boolean _isCollapseOthers;
	
	/*package*/ Node(Context context, View target, boolean isCollapseOthers) {
		this._context = context;
		this._target = target;
		_isCollapseOthers = isCollapseOthers;
		if(_target != null) {
			_target.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					click();
				}
			});
			_divider = new View(_context);
		}
	}
	
	/* package */ void setLeafClickListener(LeafClickListener listener) {
		this._listener = listener;
	}
	
	/* package */ void setDasOnGroupExpandListener(DasOnGroupExpandListener listener) {
		this._expandListener = listener;
	}
	
	/* package */ void setDasOnGroupCollapseListener(DasOnGroupCollapseListener listener) {
		this._collapseListener = listener;
	}

	/**
	 * check if this node has children nodes
	 * @return
	 */
	/*package*/ boolean hasChildren() {
		return !CollectionUtil.isEmpty(children());
	}
	
	/**
	 * check if this node is the root node
	 * @return
	 */
	/*package*/ boolean isRoot() {
		return null == _parent ? true : false;
	}
	
	/**
	 * obtain all the children nodes
	 * @return
	 */
	/*package*/ List<Node> children() {
		return _children;
	}
	
	/**
	 * find the parent of this node
	 * @return
	 */
	/*package*/ Node parent() {
		return _parent;
	}
	
	/*package*/ boolean isDisplay() {
		if(_target == null)
			return false;
		else
			return _target.getVisibility() == View.VISIBLE;
	}
	
	/**
	 * click event when click this node
	 */
	/*package*/ void click() {
		if(hasChildren()) {
			toogleChildren();
		} else {
			if(_listener != null && _target != null)
				_listener.click(_context, _target);
		}
		if(!_isCollapseOthers)
			return;
		if(level() > 1) {
			Node parent = parent();
			while(parent.level() > 1) {
				parent = parent.parent();
			}
			for(Node child : parent.parent().children()) {
				if(child.equals(parent))
					continue;
				child.closeChildren();
			}
		} else if(level() == 1) {
			Node parent = parent();
			for(Node child : parent.children()) {
				if(child.equals(this))
					continue;
				child.closeChildren();
			}
		} 
	}
	
	/*package*/ void toogleChildren() {
		if(!hasChildren()) 
			return;
		int visibility = _children.get(0).target().getVisibility();
		if(visibility == View.VISIBLE)
			closeChildren();
		else
			openChildren();
	}
	
	/**
	 * hide the children nodes of this node
	 */
	/*package*/ void closeChildren() {
		display(View.GONE);
		if(_collapseListener != null)
			_collapseListener.onGroupCollapse(_target, _level);
	}
	
	/*package*/ void openChildren() {
		display(View.VISIBLE);
		if(_expandListener != null)
			_expandListener.onGroupExpand(_target, _level);
	}
	
	private void display(int style) {
		for(int index = 0; index < _children.size(); index++) {
			Node child = _children.get(index);
			if(style != View.VISIBLE) {//close
				child.display(style);
			}
			View childView = child.target();
			childView.setVisibility(style);
			View dividerView = child.divider();
			dividerView.setVisibility(style);
		}
	}
	
	/*package*/ void setParent(Node parent) {
		this._parent = parent;
	}
	
	/*package*/ void addChild(Node node) {
		addChildAtIndex(node, _children.size());
	}
	
	/*package*/ void addChildAtIndex(Node node, int index) {
		node.setParent(this);
		node.setLevel(this._level + 1);
		_children.add(index, node);
		node.target().setVisibility(View.GONE);
		node.divider().setVisibility(View.GONE);
	}
	
	/*package*/ void addChildren(List<Node> children) {
		for(Node node : children) {
			addChild(node);
		}
	}
	
	 /**
	  * use for attach click event
	 * @return
	 */
	/*package*/ View target() {
		return _target;
	}
	
	/*package*/ View divider() {
		return _divider;
	}
	
	/*package*/ void setLevel(int level) {
		this._level = level;
	}
	
	/*package*/ int level() {
		return this._level;
	}
	
}
