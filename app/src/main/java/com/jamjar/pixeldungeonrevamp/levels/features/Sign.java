/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.jamjar.pixeldungeonrevamp.levels.features;

import com.jamjar.pixeldungeonrevamp.Assets;
import com.jamjar.pixeldungeonrevamp.Dungeon;
import com.jamjar.pixeldungeonrevamp.effects.CellEmitter;
import com.jamjar.pixeldungeonrevamp.effects.particles.ElmoParticle;
import com.jamjar.pixeldungeonrevamp.levels.DeadEndLevel;
import com.jamjar.pixeldungeonrevamp.levels.Terrain;
import com.jamjar.pixeldungeonrevamp.messages.Messages;
import com.jamjar.pixeldungeonrevamp.scenes.GameScene;
import com.jamjar.pixeldungeonrevamp.utils.GLog;
import com.jamjar.pixeldungeonrevamp.windows.WndMessage;
import com.watabou.noosa.audio.Sample;

public class Sign {

	private static final String[] teaser_texts = new String[]{
		"error RaW i work",
		"frOthinG moBs yelp",
		"CoCoOn furor rises"
	};
	
	public static void read( int pos ) {
		
		if (Dungeon.level instanceof DeadEndLevel) {
			
			GameScene.show( new WndMessage( Messages.get(Sign.class, "dead_end") ) );
			
		} else {

			if (Dungeon.depth <= 21) {
				GameScene.show( new WndMessage( Messages.get(Sign.class, "tip_"+Dungeon.depth) ) );
			} else {

				//if we are at depths 22-24 and in english
				if (Dungeon.depth-21 < 3 &&
						Messages.get(Sign.class, "burn").equals("As you try to read the sign it bursts into greenish flames.")){
					GameScene.show( new WndMessage(teaser_texts[Dungeon.depth-21]));
				}

				Dungeon.level.destroy( pos );
				GameScene.updateMap( pos );
				GameScene.discoverTile( pos, Terrain.SIGN );

				GLog.w( Messages.get(Sign.class, "burn") );

				CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
				Sample.INSTANCE.play( Assets.SND_BURNING );
			}

		}
	}
}
