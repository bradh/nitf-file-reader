/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.codice.imaging.nitf.common.TreCollection;
import org.codice.imaging.nitf.common.tre.Tre;
import org.codice.imaging.nitf.common.tre.TreEntry;
import org.codice.imaging.nitf.common.tre.TreGroup;
import org.codice.imaging.nitf.tre.TreCollectionImpl;
import org.codice.imaging.nitf.tre.TreEntryList;
import org.codice.imaging.nitf.tre.TreGroupImpl;
import org.codice.imaging.nitf.tre.TreImpl;

/**
 * TreeModel implementation for the Tagged Registered Extensions.
 */
class TreTreeModel implements TreeModel {

    private final ArrayList<TreeModelListener> treeModelListeners = new ArrayList<>();
    private final TreCollection tres;

    public TreTreeModel(final TreCollection treCollection) {
        tres = treCollection;
    }

    @Override
    public Object getRoot() {
        return tres;
    }

    @Override
    public Object getChild(final Object o, final int i) {
        if (o instanceof TreCollectionImpl) {
            TreCollectionImpl tc = (TreCollectionImpl) o;
            return tc.getTREs().get(i);
        }
        if (o instanceof Tre) {
            TreImpl tre = (TreImpl) o;
            return tre.getEntries().get(i);
        }
        if (o instanceof TreEntryList) {
            TreEntryList tel = (TreEntryList) o;
            return tel.getEntries().get(i);
        }
        if (o instanceof TreGroup) {
            TreGroupImpl tg = (TreGroupImpl) o;
            return tg.getEntries().get(i);
        }
        if (o instanceof TreEntry) {
            TreEntry te = (TreEntry) o;
            if (te.getGroups() != null) {
                return te.getGroups().get(i);
            }
        }
        return null;
    }

    @Override
    public int getChildCount(final Object o) {
        if (o instanceof TreCollectionImpl) {
            TreCollectionImpl tc = (TreCollectionImpl) o;
            return tc.getTREs().size();
        }
        if (o instanceof Tre) {
            TreImpl tre = (TreImpl) o;
            return tre.getEntries().size();
        }
        if (o instanceof TreEntryList) {
            TreEntryList tel = (TreEntryList) o;
            return tel.getEntries().size();
        }
        if (o instanceof TreGroup) {
            TreGroupImpl tg = (TreGroupImpl) o;
            return tg.getEntries().size();
        }
        if (o instanceof TreEntry) {
            TreEntry te = (TreEntry) o;
            if (te.getGroups() != null) {
                return te.getGroups().size();
            }
        }
        return 0;
    }

    @Override
    public boolean isLeaf(final Object o) {
        return (getChildCount(o) == 0);
    }

    @Override
    public void valueForPathChanged(final TreePath tp, final Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIndexOfChild(final Object o, final Object o1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTreeModelListener(final TreeModelListener tl) {
        treeModelListeners.add(tl);
    }

    @Override
    public void removeTreeModelListener(final TreeModelListener tl) {
        treeModelListeners.remove(tl);
    }

    protected void fireTreeStructureChanged(final TreCollectionImpl oldRoot) {
        TreeModelEvent e = new TreeModelEvent(this, new Object[] {oldRoot});
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeStructureChanged(e);
        }
    }
}
