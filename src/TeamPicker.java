import java.util.*;

public class TeamPicker
{
    private Set<String> names_;
    private List<Set<String>> namesTogether_;
    private List<Set<String>> namesApart_;

    public TeamPicker(Set<String> aInNames, List<Set<String>> aInNamesTogether, List<Set<String>> aInNamesApart)
    {
        names_ = aInNames;
        namesTogether_ = aInNamesTogether;
        namesApart_ = aInNamesApart;
    }

    public List<Set<String>> generateTeamsByTotalTeamCount(int aInTotalNumTeams)
    {
        return generateTeamsBySize(names_.size()/aInTotalNumTeams);
    }

    public List<Set<String>> generateTeamsBySize(int aInTeamSize)
    {
        List<Set<String>> lResult = new ArrayList<>();
        System.out.println("Generating teams from: " + getNames(names_));
        System.out.println("With the following conditions:");
        System.out.println("\tTeam size: " + aInTeamSize);
        System.out.println("\tGrouped team members: " + getNames(namesTogether_));
        System.out.println("\tSeparated team members: " + getNames(namesApart_));

        List<String> lNames = new ArrayList<>(names_);

        // First we will take care of the 'together' nstraints
        if (namesTogether_ != null && !namesTogether_.isEmpty())
        {
            namesTogether_.forEach(lTogetherNames -> {
                Set<String> lPartialTeam = new HashSet<>();
                lTogetherNames.forEach(lName -> {
                    if (lNames.contains(lName))
                    {
                        lPartialTeam.add(lName);
                        lNames.remove(lName);
                    }
                });
                lResult.add(lPartialTeam);
            });
        }

        // Next we will take care of the 'apart' constraints
        if (namesApart_ != null && !namesApart_.isEmpty())
        {
            namesApart_.forEach(lApartNames -> {
                lApartNames.forEach(lName -> {
                    if (lNames.contains(lName))
                    {
                        Set<String> lPartialTeam = new HashSet<>();
                        lPartialTeam.add(lName);
                        lNames.remove(lName);
                        lResult.add(lPartialTeam);
                    }
                });
            });
        }

        // Fill up the partial teams
        lResult.forEach(lPartialTeam -> {
            if (lPartialTeam.size() < aInTeamSize)
            {
                Collections.shuffle(lNames);

                for (int i = lPartialTeam.size(); i < aInTeamSize && !lNames.isEmpty(); ++i)
                {
                    lPartialTeam.add(lNames.remove(0));
                }
            }
        });

        // Do the rest of them
        while (!lNames.isEmpty())
        {
            lResult.add(generateTeam(lNames, aInTeamSize));
        }

        return lResult;
    }

    private Set<String> generateTeam(List<String> aInNames, int aInTeamSize)
    {
        Collections.shuffle(aInNames);

        Set<String> lTeam = new HashSet<>();
        for (int i = 0; i < aInTeamSize && !aInNames.isEmpty(); ++i)
        {
            lTeam.add(aInNames.remove(0));
        }
        return lTeam;
    }

    private String getNames(Set<String> aInNames)
    {
        if (aInNames == null || aInNames.isEmpty())
        {
            return "";
        }
        StringBuilder lSb = new StringBuilder();
        lSb.append('{');
        aInNames.forEach(lName -> lSb.append(lName).append(','));
        lSb.deleteCharAt(lSb.length()-1);
        lSb.append('}');
        return lSb.toString();
    }

    private String getNames(List<Set<String>> aInNames)
    {
        if (aInNames == null || aInNames.isEmpty())
        {
            return "";
        }

        StringBuilder lSb = new StringBuilder();

        aInNames.forEach(lNames -> lSb.append(getNames(lNames)).append(','));
        lSb.deleteCharAt(lSb.length()-1);
        return lSb.toString();
    }



    public static void main(String[] args)
    {
        Set<String> lNames = new HashSet<>(){{
            add("A"); add("B");add("C");add("D");add("E");
            add("F");add("G");add("H");add("I");add("J");
            add("K");add("L");add("M");add("N");add("O");
            add("P");add("Q");add("R");add("S");add("T");}};

        List<Set<String>> lTogetherNames = new ArrayList<>();
        lTogetherNames.add(new HashSet<>(){{add("A"); add("B");}});
        lTogetherNames.add(new HashSet<>(){{add("F"); add("S");}});

        List<Set<String>> lApartNames = new ArrayList<>();
        lApartNames.add(new HashSet<>(){{add("C"); add("B");}});
        lApartNames.add(new HashSet<>(){{add("P"); add("Q"); add("R");}});


        TeamPicker lTeamPicker = new TeamPicker(lNames, lTogetherNames, lApartNames);

        for (Set<String> lTeam : lTeamPicker.generateTeamsBySize(3))
        {
            StringBuilder lTeamSb = new StringBuilder();
            lTeam.forEach(lTeamMember -> lTeamSb.append(lTeamMember).append(','));
            lTeamSb.deleteCharAt(lTeamSb.length()-1);
            System.out.println(lTeamSb.toString());
        }

        for (Set<String> lTeam : lTeamPicker.generateTeamsByTotalTeamCount(10))
        {
            StringBuilder lTeamSb = new StringBuilder();
            lTeam.forEach(lTeamMember -> lTeamSb.append(lTeamMember).append(','));
            lTeamSb.deleteCharAt(lTeamSb.length()-1);
            System.out.println(lTeamSb.toString());
        }
    }
}