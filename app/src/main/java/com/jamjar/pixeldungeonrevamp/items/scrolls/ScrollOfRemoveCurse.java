/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.jamjar.pixeldungeonrevamp.items.scrolls;

import com.jamjar.pixeldungeonrevamp.Assets;
import com.jamjar.pixeldungeonrevamp.actors.buffs.Invisibility;
import com.jamjar.pixeldungeonrevamp.actors.buffs.Weakness;
import com.jamjar.pixeldungeonrevamp.actors.hero.Hero;
import com.jamjar.pixeldungeonrevamp.effects.Flare;
import com.jamjar.pixeldungeonrevamp.effects.particles.ShadowParticle;
import com.jamjar.pixeldungeonrevamp.items.Item;
import com.jamjar.pixeldungeonrevamp.items.bags.Bag;
import com.jamjar.pixeldungeonrevamp.messages.Messages;
import com.jamjar.pixeldungeonrevamp.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRemoveCurse extends Scroll {

	{
		initials = 8;
	}

	@Override
	protected void doRead() {
		
		new Flare( 6, 32 ).show( curUser.sprite, 2f ) ;
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		
		boolean procced = uncurse( curUser, curUser.belongings.backpack.items.toArray( new Item[0] ) );
		procced = uncurse( curUser,
			curUser.belongings.weapon,
			curUser.belongings.armor,
			curUser.belongings.misc1,
			curUser.belongings.misc2) || procced;
		
		Weakness.detach( curUser, Weakness.class );
		
		if (procced) {
			GLog.p( Messages.get(this, "cleansed") );
		} else {
			GLog.i( Messages.get(this, "not_cleansed") );
		}
		
		setKnown();

		readAnimation();
	}
	
	public static boolean uncurse( Hero hero, Item... items ) {
		
		boolean procced = false;
		for (Item item : items) {
			if (item != null && item.cursed) {
				item.cursed = false;
				procced = true;
			}
			if (item instanceof Bag){
				for (Item bagItem : ((Bag)item).items){
					if (bagItem != null && bagItem.cursed) {
						bagItem.cursed = false;
						procced = true;
					}
				}
			}
		}
		
		if (procced) {
			hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
		}
		
		return procced;
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
