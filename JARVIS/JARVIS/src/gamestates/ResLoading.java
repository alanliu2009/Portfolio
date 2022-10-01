// added a comment

package gamestates;

import org.newdawn.slick.*;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import core.Engine;
import core.Values;
import support.Resource;

import java.io.File;
import java.io.IOException;

public class ResLoading extends BasicGameState {
    private LoadingList loadingList;
    private String lastResource;

    private int id;

    private int totalTasks;
    private int tasksDone;

    public ResLoading(int id) { this.id = id; }
    
    public int getID() { return id; } // Returns the ID code for this game state

    // Initialize LoadingList
    private void initializeLoadingList(File dir, LoadingList loadingList) {
        for(final File file: dir.listFiles()) {
            if(file.isDirectory()) {
                initializeLoadingList(file, loadingList);
            } else {
                loadingList.add(new Resource(file));
            }
        }
    }

    // Initializer, first time
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
    {
        gc.setShowFPS(true);

        this.loadingList = LoadingList.get();
    }

    @Override // Begin file loading upon entering the gamestate
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        // Set Loading List to Deferred
        LoadingList.setDeferredLoading(true);

        // Initialize Loading List
        initializeLoadingList(new File(Values.Res_Folder), loadingList);

        this.totalTasks = loadingList.getTotalResources();
        this.tasksDone = 0;
    }



    @Override // Update, runs consistently
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
    {
        // Load a new resource
        if(loadingList.getRemainingResources() > 0) {
            try {
                DeferredResource resource = loadingList.getNext();
                resource.load();
                lastResource = resource.getDescription();
            } catch(IOException e) {
                System.out.println("Failed to load a resource");
            }
        }
        // When loading is completed, automatically move to start menu
        else {
            sbg.enterState(Engine.StartingMenu_ID);
        }
    }

    @Override // Render, all visuals
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
    {
        // Calculate the number of tasks done
        this.tasksDone = loadingList.getTotalResources() - loadingList.getRemainingResources();

        // Draw a Loading Bar
        final float BAR_WIDTH = gc.getWidth() - 0.25f * gc.getWidth();
        final float BAR_HEIGHT = 0.0926f * gc.getHeight();

        final float BAR_X = gc.getWidth() / 2 - BAR_WIDTH / 2;
        final float BAR_Y = gc.getHeight() / 2 - BAR_HEIGHT / 2;

        final float PERCENT_LOADED = (float) tasksDone / (float) totalTasks;

        // max loading bar
        g.setColor(new Color(0, 100, 0, 150));
        g.fillRect(BAR_X, BAR_Y, BAR_WIDTH, BAR_HEIGHT);

        // current loaded
        g.setColor(new Color(0, 255, 0, 150));
        g.fillRect(BAR_X, BAR_Y, BAR_WIDTH * PERCENT_LOADED, BAR_HEIGHT);

        // white outline
        g.setColor(new Color(255, 255, 255));
        g.drawRect(BAR_X, BAR_Y, BAR_WIDTH, BAR_HEIGHT);

        g.drawString("Loaded " + lastResource, BAR_X + BAR_WIDTH / 2 - 35f, BAR_Y + BAR_HEIGHT + 25f);
    }
}
