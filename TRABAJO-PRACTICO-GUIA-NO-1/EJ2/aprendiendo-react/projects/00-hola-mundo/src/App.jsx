import './App.css'
import { TwitterFollowCard } from './TwitterFollowCard'

const users = [
  {
    userName: 'midudev',
    name: 'Miguel Ángel Durán',
    initialIsFollowing: true
  },
  {
    userName: 'pheralb',
    name: 'Pablo Hernandez'
  },
  {
    userName: 'willyrexYT',
    name: 'Guillermo Díaz'
  },
  {
    userName: 'Vegetta777',
    name: 'Samuel De Luque'
  }
]

export function App() {
  const formatUserName = (userName) => `@${userName}`

  return (
    <section className='App'>
      {users.map(user => {
        const { userName, name, initialIsFollowing } = user
        return (
          <TwitterFollowCard
            key={userName}
            formatUserName={formatUserName}
            userName={userName}
            name={name}
            initialIsFollowing={initialIsFollowing}
          >
            {name}
          </TwitterFollowCard>
        )
      })}
    </section>
  )
}