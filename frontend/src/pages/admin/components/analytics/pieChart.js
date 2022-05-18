import { PieChart, Pie, Cell, ResponsiveContainer } from 'recharts';
import { getData } from "@api/api";
import { SERVER_USER_AGE } from "@endpoints";
import { LOGOUT } from "@constants";
import { useNavigate } from 'react-router';
import { useEffect, useState } from 'react';

const PieChartComponent = () => {
  const [data, setData] = useState()
  const navigate = useNavigate()

  useEffect(() => {
    async function fetchData() {
      const response = await getData(SERVER_USER_AGE)
      if(response.logout) {
        navigate(LOGOUT)
      }
      if(response.isSuccess) {
        setData([
          {
            "name": "Teen",
            "value": response.teen,
            color: "#f0c420",
          },
          {
            "name": "Adult",
            "value": response.adult,
            color: "#86DC3D",
          },
          {
            "name": "Middle age adult",
            "value": response.middleAge,
            color: "#417505",
          },
          {
            "name": "Senior",
            "value": response.senior,
            color: "#c09d1a",
          },
          {
            "name": "Unfilled",
            "value": response.unfilled,
            color: "#d3d3d3",
          },
        ])
      }
    }

    fetchData()
  }, [navigate])

  return (
    <>
      <h2>Users age structure</h2>
      {data &&
      <ResponsiveContainer width="100%" aspect={3}>
        <PieChart width={400} height={400} style={{margin: "auto"}}>
          <Pie data={data} dataKey="value" nameKey="name" cx="50%" cy="50%"
            outerRadius="80%" fill="#ff0000" paddingAngle={2}
            labelLine={false}
            label={({
                cx,
                cy,
                midAngle,
                innerRadius,
                outerRadius,
                value,
                index
              }) => {
                const RADIAN = Math.PI / 180;
                const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
                const x = cx + radius * Math.cos(-midAngle * RADIAN);
                const y = cy + radius * Math.sin(-midAngle * RADIAN);
      
                return (
                  <text
                    x={x}
                    y={y}
                    fill="#9B9B9B"
                    textAnchor={x > cx ? "start" : "end"}
                    dominantBaseline="central"
                    display={data[index].value === 0 ? "none" : null}
                    style={{fontWeight: "bold"}}
                  >
                    {value.toFixed(2)}%
                  </text>
                );
              }}>
            {
              data.map((entry, index) => <Cell key={index} fill={entry.color}/>)
            }
          </Pie>
        </PieChart>
      </ResponsiveContainer>}
      <ol style={{textAlign: "left", width: "100%"}}>
        <li style={{color: "#f0c420", fontWeight: "bold"}}>Teens: 18-19 yo</li>
        <li style={{color: "#86DC3D", fontWeight: "bold"}}>Adults: 20-39 yo</li>
        <li style={{color: "#417505", fontWeight: "bold"}}>Middle age adults: 40-59 yo</li>
        <li style={{color: "#c09d1a", fontWeight: "bold"}}>Seniors: 60+ yo</li>
        <li style={{color: "#9B9B9B", fontWeight: "bold",
          opacity: "90%"}}>Unfilled: did not enter birth data</li>
      </ol>
    </>
  )
}

export default PieChartComponent
