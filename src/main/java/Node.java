import java.util.Arrays;
import java.util.Random;
import java.lang.Math;
public class Node extends Thread
{
    public  int location;
    private int identifier;
    private Thread thread;
    Random rand = new Random();
    boolean leftSide = false;
    boolean collision = false;
    int failedTransmissions = 0;
    double power = 1;


    Node(int identifier, int location)
    {
        this.identifier = identifier;
        this.location = location;
    }

    public void transmit()
    {
        int randomNode = identifier;
        while (randomNode == identifier)
        {
            randomNode = rand.nextInt(((Main.numberOfNodes-1) - 0) + 1) + 0;
        }

        System.out.println("[ " + identifier + " ] Node " + identifier + " will transmit to node " + randomNode);

        try
        {
            this.sleep(2000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        double s = Main.locationOfNodes[randomNode] - Main.locationOfNodes[identifier];
        if (s < 0)
        {
            leftSide = true;
            s = -s;
        }
        double t = s / 300000;

        double littleTime = 1/300000.0;
        double timeOfCurrentTravel = 0;

        collision = false;

        if (leftSide == false)
        {
            System.out.println("[ " + identifier + " ] - Right");
            for (int i = location; i < Main.locationOfNodes[randomNode]; i++)
            {
                if(Main.jam == true)
                {
                    failedTransmissions++;
                    System.out.println("|JAM| [ " + identifier + " ]");
                    Main.nodesNotifiedAboutJam++;

                    if (Main.nodesNotifiedAboutJam == Main.numberOfNodes)
                    {
                        System.out.println("everyone notified about JAM");
                        Main.nodesNotifiedAboutJam = 0;
                        Main.jam = false;
                    }
                    power++;
                    int delay = rand.nextInt(((int)Math.pow(2.0, power) - 1) + 1) + 1;

                    try
                    {
                        this.sleep((long) (t * delay));
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    collision = true;
                    break;
                }
                if (Main.cable[location] != 0)
                {
                    failedTransmissions++;
                    System.out.println("|Medium is busy| [ " + identifier + " ] Waiting");
                    collision = true;
                    break;
                }

                if (Main.cable[i+1] == 0)
                {
                    System.out.println("[ " + identifier + " ] - cable[" + (i+1) + "] = " + Main.cable[i+1]);
                    Main.cable[i+1] = 1;

                    timeOfCurrentTravel += littleTime;
                }
                else
                {
                    failedTransmissions++;

                    try
                    {
                        this.sleep((long) timeOfCurrentTravel*2);

                        if (Main.jam != true)
                        {
                            Main.jam = true;
                            System.out.println("[ " + identifier + " ] sending |JAM| signal");
                            Main.nodesNotifiedAboutJam++;
                        }
                        else
                        {
                            System.out.println("|JAM| [ " + identifier + " ]");
                            Main.nodesNotifiedAboutJam++;
                            if (Main.nodesNotifiedAboutJam == Main.numberOfNodes) {
                                System.out.println("everyone notified about JAM");
                                Main.nodesNotifiedAboutJam = 0;
                                Main.jam = false;
                            }
                            if (power < 10) power++;
                            int delay = rand.nextInt(((int)Math.pow(2.0, power) - 1) + 1) + 1;
                            this.sleep((long) (t * delay));
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    collision = true;
                    break;
                }
            }

            if (collision == false)
            {
                failedTransmissions = 0;
                power = 1;
                System.out.println("success | data wil be transmitted from " + identifier + " to " + randomNode);

                Arrays.fill(Main.cable, 0);
                System.out.println("[ " + identifier + " ] Sending data...");
                try
                {
                    this.sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Collision with " + identifier + " to " + randomNode);
                Arrays.fill(Main.cable, 0);
            }

        }
        else
        {
            System.out.println("[ " + identifier + " ] - LEFT");
            for (int i = location; i > Main.locationOfNodes[randomNode]; i--)
            {
                if(Main.jam == true)
                {
                    failedTransmissions++;
                    System.out.println("|JAM| [ " + identifier + " ]");
                    Main.nodesNotifiedAboutJam++;

                    if (Main.nodesNotifiedAboutJam == Main.numberOfNodes)
                    {
                        System.out.println("everyone notified about JAM");
                        Main.nodesNotifiedAboutJam = 0;
                        Main.jam = false;
                    }
                    power++;
                    int delay = rand.nextInt(((int)Math.pow(2.0, power) - 1) + 1) + 1;

                    try
                    {
                        this.sleep((long) (t * delay));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    collision = true;
                    break;
                }

                if (Main.cable[location] != 0)
                {
                    System.out.println("|Medium is busy| [ " + identifier + " ] Waiting");
                    failedTransmissions++;
                    collision = true;
                    break;
                }

                if (Main.cable[i-1] == 0) {
                    System.out.println("[ " + identifier + " ] - cable[" + (i-1) + "] = " + Main.cable[i-1]);
                    Main.cable[i-1] = 1;

                    timeOfCurrentTravel += littleTime;
                }
                else
                {
                    failedTransmissions++;
                    System.out.println("[ " + identifier + " ] Encountered collision. Travel time = " + timeOfCurrentTravel);
                    try
                    {
                        this.sleep((long) timeOfCurrentTravel*2);

                        if (Main.jam != true)
                        {
                            Main.jam = true;
                            System.out.println("[ " + identifier + " ] sending signal |JAM|");
                            Main.nodesNotifiedAboutJam++;

                        }
                        else
                        {
                            System.out.println("|JAM| [ " + identifier + " ]");
                            Main.nodesNotifiedAboutJam++;
                            if (Main.nodesNotifiedAboutJam == Main.numberOfNodes)
                            {
                                System.out.println("everyone notified about JAM");
                                Main.nodesNotifiedAboutJam = 0;
                                Main.jam = false;
                            }

                            if (power < 10)
                                power++;

                            int delay = rand.nextInt(((int)Math.pow(2.0, power) - 1) + 1) + 1;

                            this.sleep((long) (t * delay));
                        }
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    collision = true;
                    break;
                }
            }

            if (collision == false)
            {
                System.out.println("success | data fill be transmitted from " + identifier + " to " + randomNode);
                failedTransmissions = 0;
                power = 1;

                Arrays.fill(Main.cable, 0);
                System.out.println("[ " + identifier + " ] Sending data...");
                try
                {
                    this.sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Collision with " + identifier + " to " + randomNode);
                Arrays.fill(Main.cable, 0);
            }
        }
    }

    public void run()
    {
        try
        {
            int delay = rand.nextInt((5 - 1) + 1) + 1;
            this.sleep(2000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        while (true)
        {
            if (failedTransmissions == 16)
            {
                System.out.println("[ " + identifier + " ] - 16 collisions -> Node finished transmitting.");
                break;
            }
            transmit();
        }
    }

    public void start()
    {
        thread = new Thread(this, "Node");
        thread.start();
        System.out.println("Node " + identifier + " started");
    }
}